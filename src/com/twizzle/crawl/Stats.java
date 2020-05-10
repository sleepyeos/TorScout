package com.twizzle.crawl;

import java.util.HashMap;
import java.util.Properties;

public class Stats {

	public static void main(String[] args) {
		
		System.out.println("[RINNDEX STATS 4.01]");
		System.out.println("[i] initializing...");
		
		Crawler.getInstance();
		Properties props = ConfigurationManager.loadConfig();
		String rindexPath = props.getProperty("rindex.path");
		
		@SuppressWarnings("unchecked")
		HashMap<String, CrawlResult> rindex = 
				(HashMap<String, CrawlResult>) SerializationManager.load(rindexPath);

		/* Display rindex stats */
		System.out.println("Reverse Index Stats: ");
		System.out.println("Keywords indexed: " + rindex.size());
		
		
	}

}
