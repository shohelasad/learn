package org.learn.infra.rss;

import static br.com.caelum.vraptor.environment.EnvironmentType.DEVELOPMENT;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.joda.time.DateTimeUtils;
import org.junit.Test;
import org.learn.builder.QuestionBuilder;
import org.learn.dao.TestCase;
import org.learn.infra.rss.write.QuestionRssEntryFactory;
import org.learn.infra.rss.write.RssFeedFactory;
import org.learn.model.Question;
import org.learn.model.User;
import org.learn.model.interfaces.RssContent;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.EnvironmentType;

public class RssFeedFactoryTest extends TestCase {
	
	@Test
	public void should_generate_feed() throws IOException {
		DefaultEnvironment env = new DefaultEnvironment(new EnvironmentType("learn"));
		QuestionRssEntryFactory factory = new QuestionRssEntryFactory(env);
		RssFeedFactory rssFeedFactory = new RssFeedFactory(env, factory);
		
		QuestionBuilder builder = new QuestionBuilder();
		
		DateTimeUtils.setCurrentMillisFixed(100);
		User user1 = user("author1", "author@email");
		user1.setPhotoUri(new URL("http://imagemsuser1.com"));
		Question question1 = builder.withAuthor(user1)
			.withTitle("first question")
			.withDescription("question")
			.withId(1l)
			.build();
		
		User user2 = user("author2", "author@email");
		user2.setPhotoUri(new URL("http://imagemsuser2.com"));
		Question question2 = builder.withId(2l)
			.withTitle("second question")
			.withAuthor(user2)
			.build();
		
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		rssFeedFactory.build(Arrays.<RssContent>asList(question1, question2), output, "title", "description");
		output.close();
		String xml = new String(output.toByteArray());
		assertTrue(xml.contains("first question"));
		assertTrue(xml.contains("second question"));
		assertTrue(xml.contains("http://imagemsuser1.com"));
		assertTrue(xml.contains("http://imagemsuser2.com"));
		DateTimeUtils.setCurrentMillisSystem();
	}
}
