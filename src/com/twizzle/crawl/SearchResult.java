package com.twizzle.crawl;

public class SearchResult {
	
	public CrawlResult cr;
	public double relevance;
	
	public String toString() {
		return cr.toString() + "\nrelevance:" + relevance + '\n'; 
	}
	
	public double getRelevance() {
		
		return this.relevance;
	}

}
