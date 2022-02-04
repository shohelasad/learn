package org.learn.sanitizer;

public class QuotesSanitizer {

	public static String sanitize(String string){
		return string.replaceAll("\"","");
	}
}
