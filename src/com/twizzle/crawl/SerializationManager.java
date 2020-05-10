package com.twizzle.crawl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializationManager {
	
	public static void dump(Object o, String filename) {

		try {
			FileOutputStream fos = new FileOutputStream(filename);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(o);
			oos.flush();
			oos.close();
			fos.close();
			
		} catch(IOException e) {
			e.printStackTrace();
		} 
	}
	
	public static Object load(String path) {
		try {
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object o = ois.readObject();
			ois.close();
			fis.close();
			
			return o;
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean exists(String filename) {
		File f = new File(filename);
		return f.exists() && !f.isDirectory();
	}

}
