package com.twizzle.crawl;

import java.io.Serializable;

public class CrawlResult implements Serializable {
	
	private static final long serialVersionUID = -4135977783839124434L;
	public String title;
	public String url;
	public String[] tokens;
	
	@Override
	public String toString() {
		return "[" + url + "]:  " + title;
	}
}
