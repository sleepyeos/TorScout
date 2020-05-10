package com.twizzle.crawl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class TorConnectionTester {
	
	private static final String url = "https://check.torproject.org";
	
	public static void main(String[] args) {
		System.out.println("Connected to tor network: " + test());
	}
	
	public static boolean test() {
		try {
			Connection conn = Jsoup.connect(url);
			Document doc = conn.get();
			if(doc.title().contains("Congratulations")) {
				return true;
			}
		} catch (Exception e) {
			
		}
		
		return false;
	}

}
