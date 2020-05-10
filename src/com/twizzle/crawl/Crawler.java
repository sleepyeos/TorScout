package com.twizzle.crawl;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler implements Serializable {
	
	private static final long serialVersionUID = 1991223817932229515L;

	private String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; rv:68.0) Gecko/20100101 Firefox/68.0";
	
	public List<String> queue;
	private Set<String> history;
	private List<CrawlResult> index;
	private int crawlDelay;
	private String urlFilter;
	
	public Crawler() {
		this.queue = new LinkedList<String>();
		this.history = new HashSet<String>();
		this.index = new LinkedList<CrawlResult>();

	}
	
	public List<CrawlResult> getIndex() {
		return this.index;
	}
	
	public static Crawler getInstance() {
		System.out.println("[RINNDEX CRAWLER 4.01]");
		System.out.println("[i] initializing...");
		Properties props = ConfigurationManager.loadConfig();
		String crawlerPath = props.getProperty("crawlerobj.path");
		String urlFilter = props.getProperty("url.filter");
		int crawlDelay = Integer.parseInt(props.getProperty("crawler.delay"));
		
		if(SerializationManager.exists(crawlerPath)) {
			Crawler loadedCrawler = (Crawler) SerializationManager.load(crawlerPath);
			System.out.println("[i] Successfully loaded crawler object from disk");
			loadedCrawler.printStatus();
			loadedCrawler.crawlDelay = crawlDelay;
			loadedCrawler.urlFilter = urlFilter;

			return loadedCrawler;
		} 
		
		System.out.println("[e] could not load crawler object from disk, using clean instance\n\n");
		Crawler newCrawler = new Crawler();
		newCrawler.crawlDelay = crawlDelay;
		newCrawler.urlFilter = urlFilter;
		return new Crawler();
		
	}
	
	public void printStatus() {
		System.out.println("[queue]: " + this.queue.size());
		System.out.println("[history]: " + this.history.size());
		System.out.println("[index]: " + this.index.size());
		System.out.println("\n");
	}
	
	public void crawl(String seed) throws InterruptedException {
		
		boolean connectedToTor = TorConnectionTester.test();
		
		if(!connectedToTor) {
			System.err.println("[e] could not reach tor network, shutting down\n");
			System.exit(1);
		} else {
			System.out.println("[i] tor connection established\n");
		}
		
		this.queue.add(seed);
		
		while(this.hasNextUrl()) {
			
			String currentUrl = this.nextUrl();
			Document doc = this.fetchDocument(currentUrl);
			
			if(doc != null && ContentFilter.passesFilter(getTokens(doc))) {
				List<String> links = this.getLinks(doc);
				String[] tokens = this.getTokens(doc);
				
				this.queue.addAll(links);
				
				CrawlResult cr = new CrawlResult();
				cr.tokens = tokens;
				cr.title = doc.title();
				cr.url = currentUrl;
				
				System.out.println(cr);
				this.index.add(cr);
			} else {
				System.out.println("[-] " + currentUrl);
			}
			
			this.history.add(currentUrl);

			
			this.printStatus();
			Thread.sleep(this.crawlDelay);
		}
		
	}
	
	private Document fetchDocument(String url) {
		Connection conn = Jsoup.connect(url).userAgent(USER_AGENT);
		Document doc;
		try {
			doc = conn.timeout(10*1000).get();
			if(conn.response().contentType().contains("text/html")) {
				return doc;
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	private boolean hasNextUrl() {
		return this.queue.size() > 0;
	}
	
	private String nextUrl() {
		String next;
		do {
			next = this.queue.remove(0);
		} while (this.history.contains(next));
		
		return next;
	}
	
	private List<String> getLinks(Document d) {
		List<String> links = new LinkedList<String>();
		
		Elements linksOnPage = d.select("a[href]");
		
		for(Element l : linksOnPage) {
			String link = l.absUrl("href");
			link = link.replaceAll("[\\?#].*", "");
			link = link.replaceAll("\\?[a-zA-Z0-9_=&-]*", "");
			
			if(link.contains(".onion")) {
				links.add(link);
			}
		}
		
		return links;
	}
	
	private String[] getTokens(Document d) {
		String body = d.body().text() + " " + d.title();
		return body.toLowerCase().split("\\P{L}+");
	}
	
	public void addToQueue(String url) {
		this.queue.add(url);
	}
	
	public List<String> getQueue() {
		return this.queue;
	}
	
	public void removeFromQueue(String url) {
		for(String u : this.queue) {
			if(url.equals(u)) {
				this.queue.remove(u);
			}
			
		}
	}
	
	public void removeBySubstring(String subs) {
		LinkedList<String> toRemove = new LinkedList<String>();
		
		for(String u : this.queue) {
			if(u.toLowerCase().contains(subs.toLowerCase())) {
				toRemove.add(u);
			}
		}
		
		this.queue.removeAll(toRemove);
	}
	

}
