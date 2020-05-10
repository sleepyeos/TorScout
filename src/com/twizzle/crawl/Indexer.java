package com.twizzle.crawl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Set;

public class Indexer {
	
	private static HashMap<String, LinkedList<CrawlResult>> rindex = new HashMap<String, LinkedList<CrawlResult>>();
	
	public void buildRindex() {
		System.out.println("[RINNDEXER 4.01]");
		System.out.println("[info] attempting load from disk");
		Properties props = ConfigurationManager.loadConfig();
		String crawlerObjPath = props.getProperty("crawlerobj.path");
		String rindexPath = props.getProperty("rindex.path");
		Crawler c = (Crawler) SerializationManager.load(crawlerObjPath);
		System.out.println("[info] Successfully loaded crawler from disk");
		
		for(CrawlResult cr : c.getIndex()) {
			System.out.println("[+] Indexing: " + cr);
			String[] tokens = cr.tokens;
			Set<String> tokenSet = new HashSet<String>();
			
			for(String token : tokens) {
				token = token.replace('.', ' ')
						.replace(',', ' ')
						.replace('!', ' ')
						.replace('?', ' ')
						.replace('\'', ' ')
						.replace('"', ' ')
						.replace('(', ' ')
						.replace(')', ' ')
						.toLowerCase();
				
				tokenSet.add(token);
			}
			
			for(String token : tokenSet) {
				if(rindex.containsKey(token)) {
					rindex.get(token).add(cr);
				} else {
					LinkedList<CrawlResult> results = new LinkedList<CrawlResult>();
					results.add(cr);
					rindex.put(token, results);
				}
			}
		}
		
		System.out.println("[info] Reverse index constructed. Attempting save to disk");
		SerializationManager.dump(rindex, rindexPath);
		System.out.println("[info] save successful");
	}
	
	public static void main(String[] args) { 
		
		Indexer ind = new Indexer();
		ind.buildRindex();

	}

}
