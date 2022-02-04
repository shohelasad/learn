package org.learn.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.learn.model.TagDictionary;

public class TagDictionaryDAO {
	
	private static String fileName = "word.txt";
	private static HashMap<String, String> dictionary;
	private Session session;

	@Deprecated
	public TagDictionaryDAO() {
	}

	@Inject
	public TagDictionaryDAO(Session session) {
		this.session = session;
	}
	
	public void prepareTagList(){
		dictionary = new HashMap<>();
		try {
			ClassLoader classLoader = getClass().getClassLoader();
		    InputStream is = classLoader.getResourceAsStream(fileName);
		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
		    String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(":");
				dictionary.put(tokens[0], tokens[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return;
	}
	
	public String getTags(String key){	
		return dictionary.get(key);
	}
}
