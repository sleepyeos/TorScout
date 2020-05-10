package com.twizzle.crawl;

import java.util.LinkedList;

public class Test {

	public static void main(String[] args) {

		LinkedList<String> asdf = new LinkedList<String>();
		asdf.add("hello,moon".substring(2));
		System.out.println(asdf.get(0));
		
		asdf.remove("llo,moon");
		
		System.out.println(asdf.size());
		
	}

}
