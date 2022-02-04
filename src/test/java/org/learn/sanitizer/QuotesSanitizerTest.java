package org.learn.sanitizer;

import static org.junit.Assert.assertEquals;
import static org.learn.sanitizer.QuotesSanitizer.sanitize;

import org.junit.Test;

public class QuotesSanitizerTest {
	
	@Test
	public void should_remove_quotes() throws Exception {
		String string = "My description = \"test\"; ";
		assertEquals("My description = test; ", sanitize(string));
	}
}
