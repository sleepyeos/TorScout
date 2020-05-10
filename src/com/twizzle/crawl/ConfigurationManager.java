package com.twizzle.crawl;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationManager {
	
	public static final String CONFIG_PATH = "crawler.config";
	
	public static Properties loadConfig() {
		
		return loadConfig(CONFIG_PATH);
	}
	
	public static Properties loadConfig(String filename) {
		
		Properties prop = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream(filename);
			prop.load(fis);
			return prop;
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		return null;
		
	}

}
