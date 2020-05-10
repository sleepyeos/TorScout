package com.twizzle.crawl;

import java.util.Properties;

public class ContentFilter {
	
	public static String[] STOP_WORDS;
	
	public static boolean passesFilter(String[] tokens) {
		
		if(STOP_WORDS == null) {
			Properties props = ConfigurationManager.loadConfig();
			STOP_WORDS = props.getProperty("crawler.stopwords").split(",");
		}
		
		for(String w : STOP_WORDS) {
			for(String s : tokens) {
				if(s.equals(w)) {
					return false;
				}
			}
		}
		
		return true;
	}

}
