package com.twizzle.crawl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Searcher {
	
	public HashMap<String, LinkedList<CrawlResult>> rindex;
	public int indexCount;
	public static final int numResults = 10;
	
	@SuppressWarnings("unchecked")
	public Searcher() {
		System.out.println("[RINNDEX SEARCHER 4.01]");
		System.out.println("[i] initializing...\n");
		Properties props = ConfigurationManager.loadConfig();
		String rindexPath = props.getProperty("rindex.path");
		String crawlerPath = props.getProperty("crawlerobj.path");
		this.rindex = (HashMap<String, LinkedList<CrawlResult>>) SerializationManager.load(rindexPath);
		
		Crawler c = (Crawler) SerializationManager.load(crawlerPath);
		this.indexCount = c.getIndex().size();
	}
	
	public List<SearchResult> search(String query) {
		List<SearchResult> results = new LinkedList<SearchResult>();
		
		if(query.equals("")) {
			return results;
		}
		
		List<CrawlResult> exclude = new LinkedList<CrawlResult>();
		
		String[] terms = query.toLowerCase().split("\\s");
		
		for(String term : terms) {
			if(term.length() > 0 && term.charAt(0) != '-') {
				if(rindex.containsKey(term)) {
					LinkedList<CrawlResult> termResults = rindex.get(term);
					for (CrawlResult cr : termResults) {
						SearchResult newSearchResult = new SearchResult();
						newSearchResult.cr = cr;
						newSearchResult.relevance = relevance(term, cr);
						results.add(newSearchResult);
					}
				}
			} else {
				term = term.replace("-", "");
				if(rindex.containsKey(term)) {
					LinkedList<CrawlResult> termResults = rindex.get(term);
					exclude.addAll(termResults);
				}
			}
		}
		
		LinkedList<SearchResult> toRemove = new LinkedList<SearchResult>();
		
		for(CrawlResult cr : exclude) {
			for(SearchResult sr : results) {
				if(sr.cr == cr) {
					toRemove.add(sr);
				}
			}
		}
		
		for(SearchResult sr : toRemove) {
			results.remove(sr);
		}
		
		Collections.sort(results, Comparator.comparingDouble(SearchResult ::getRelevance));
		Collections.reverse(results);
		
		return results;
	}
	
	public double relevance(String term, CrawlResult cr) {
		int occurrances = 1;
		for(String token : cr.tokens) {
			if (token.equals(term)) {
				occurrances++;
			}
		}
		
		double termFrequency = 1 + Math.log(occurrances);
		if(cr.title.toLowerCase().contains(term)) {
			termFrequency *= 10;
		}
		
		
		int documentOccurrances = this.rindex.get(term).size();
		double idf = this.indexCount / documentOccurrances;
		
		return idf * termFrequency;
	}
	
	public static void main(String[] args) { 
		Searcher s = new Searcher();
		Scanner scan = new Scanner(System.in);
		
		while(true) {	
			System.out.println("*************************************");
			System.out.println("*              Search               *");
			System.out.println("*************************************");
			System.out.print("> ");
			String query = scan.nextLine();
			List<SearchResult> results = s.search(query);
						
			for(int i = 0; i < numResults; i++) {
				if(i >= results.size()) {
					break;
				}
				System.out.println(results.get(i));
			}
		}
	}

}
