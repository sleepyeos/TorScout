package com.twizzle.crawl;

import java.io.IOException;
import java.util.Properties;


public class CrawlerDriver {
	public static void main(String[] args) throws IOException, InterruptedException {
		
		Properties props = ConfigurationManager.loadConfig();
		String crawlerPath = props.getProperty("crawlerobj.path");
		String seed = props.getProperty("crawler.seed");
		Crawler c = Crawler.getInstance();
		
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("[+] shutdown hook: saving crawler object");
				SerializationManager.dump(c, crawlerPath);
			}
		});
		
		
		c.crawl(seed);
		
		
	}
}
