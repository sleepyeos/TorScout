package com.twizzle.crawl;

import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class Editor {
	
	public static Crawler crawler;
	public static String crawlerPath;
	public static Scanner scan;
	
	public static void displayMenu() {
		crawler.printStatus();
		System.out.println("Select:");
		System.out.println("[1] Add to queue");
		System.out.println("[2] Purge queue by URL Substring");
		System.out.println("[3] Purge index by query");
		System.out.println("[4] exit");
		System.out.println("[5] view queue head");

	}
	
	public static void commit() {
		System.out.println("[i] attempting save");
		SerializationManager.dump(crawler, crawlerPath);
		System.out.println("[i] save successful");

	}
	
	public static void addToQueue() {
		System.out.println("Enter URL to add to queue");
		
		String response = scan.nextLine().trim();
		crawler.addToQueue(response);
		commit();
		
	}
	
	public static void viewQueueHead() {
		System.out.println("Displaying queue head");
		for(int i = 0; i < 100; i++) {
			System.out.println(crawler.queue.get(i));
		}
	}
	
	
	public static void purgeUrlSubstring() {
		System.out.println("Enter url substring to purge:");
		String response = scan.nextLine().trim();
		System.out.println("Purging: \"" + response +"\"");
		crawler.removeBySubstring(response);
		commit();
	}
	
	public static void purgeByKeyword() {
		System.out.println("Enter search query to purge:");
		String response = scan.nextLine().trim();
		Searcher s = new Searcher();
		List<SearchResult> results = s.search(response);
		
		for(SearchResult sr : results) {
			
			crawler.getIndex().remove(sr.cr);
		}
		
		System.out.println("Purged " + results.size() + " results");
	}

	public static void main(String[] args) {
		System.out.println("[RINNDEX EDITOR 4.01]");
		System.out.println("[i] initializing....");
		
		Properties props = ConfigurationManager.loadConfig();
		crawlerPath = props.getProperty("crawlerobj.path");
		crawler = (Crawler) SerializationManager.load(crawlerPath);
		scan = new Scanner(System.in);
		
		while(true) {
			displayMenu();
			
			try {
				String response = scan.nextLine();
				int choice = Integer.parseInt(response.trim());
				
				switch(choice) {
				case 1:
					addToQueue();
					break;
				case 2:
					purgeUrlSubstring();
					break;
				case 3:
					purgeByKeyword();
					break;
				case 4:
					System.exit(0);
				case 5:
					viewQueueHead();
				}
				
				
			} catch (Exception e) {
				System.err.println("[e] invalid input\n");
			}
		}
		
	}

}
