package org.learn.dao;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.learn.builder.QuestionBuilder;
import org.learn.dao.TagDAO;
import org.learn.model.Answer;
import org.learn.model.Question;
import org.learn.model.Tag;
import org.learn.model.TagUsage;
import org.learn.model.User;


public class TagDAOTest extends DatabaseTestCase{

	private TagDAO tags;
	private User leo;
	private Tag java;
	private Tag ruby;
	private Tag scala;
	private Tag groovy;


	@Before
	public void setup() {
		this.tags = new TagDAO(session);
		leo = user("leonardo", "leo@leo");
		java = new Tag("java", "", leo);
		ruby = new Tag("ruby", "", leo);
		scala = new Tag("scala", "", leo);
		groovy = new Tag("groovy", "", leo);
		session.save(leo);
		session.save(scala);
		session.save(groovy);
		session.save(java);
		session.save(ruby);
	}

	@Test
	public void should_load_recent_tags_used() throws Exception {
        DateTimeUtils.setCurrentMillisFixed(new DateTime().minusMonths(3).getMillis());
        questionWith(Arrays.asList(java));
        DateTimeUtils.setCurrentMillisSystem();
        questionWith(Arrays.asList(java));
		questionWith(Arrays.asList(java));
		questionWith(Arrays.asList(ruby));
		questionWith(Arrays.asList(scala));

		List<TagUsage> recentTagsUsage = tags.getRecentTagsSince(new DateTime().minusMonths(2), 2);
		
		assertEquals(2, recentTagsUsage.size());
		assertEquals(2L, recentTagsUsage.get(0).getUsage().longValue());
		assertEquals(1L, recentTagsUsage.get(1).getUsage().longValue());
		assertEquals(java.getId(), recentTagsUsage.get(0).getTag().getId());
		assertEquals(ruby.getId(), recentTagsUsage.get(1).getTag().getId());
	}
	
	@Test
	public void should_get_main_tags_of_the_provided_user() throws Exception {
		Question javaQuestion = questionWith(Arrays.asList(java));
		Answer javaAnswer = answer("just do this and that and you will be happy forever", javaQuestion, leo);
		session.save(javaAnswer);
		
		Question oneMoreJavaQuestion = questionWith(Arrays.asList(java));
		Answer oneMoreJavaAnswer = answer("just do this and that and you will be happy forever", oneMoreJavaQuestion, leo);
		session.save(oneMoreJavaAnswer);
		
		Question rubyQuestion = questionWith(Arrays.asList(ruby));
		Answer rubyAnswer = answer("just do this and that and you will be happy forever", rubyQuestion, leo);
		session.save(rubyAnswer);
		
		User chico = user("chicoh", "chico@chico.com");
		session.save(chico);
		Question otherJavaQuestion = questionWith(Arrays.asList(java));
		Answer otherJavaAnswer = answer("just do this and that and you will be happy forever", otherJavaQuestion, chico);
		session.save(otherJavaAnswer);
		
		List<TagUsage> mainTags = tags.findMainTagsOfUser(leo);
		
		assertEquals(2, mainTags.size());
		assertEquals(2L, mainTags.get(0).getUsage().longValue());
		assertEquals(1L, mainTags.get(1).getUsage().longValue());
	}
	
	@Test
	public void should_get_all_tag_names() throws Exception {
		List<String> tagsNames = tags.allNames();
		assertEquals(4, tagsNames.size());
		assertTrue(tagsNames.containsAll(Arrays.asList("java", "groovy", "ruby", "scala")));
	}
	
	@Test
	public void should_get_empty_list_for_nonexistant_names() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("blabla", "lala"));
		assertTrue(found.isEmpty());
	}
	
	@Test
	public void should_split_two_spaces() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("java", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}
	
	@Test
	public void should_find_tags_ordered() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("ruby", "java"));
		assertEquals(2, found.size());
		assertEquals("ruby", found.get(0).getName());
		assertEquals("java", found.get(1).getName());
	}
	
	@Test
	public void should_not_repeat_tags() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("java", "java", "ruby", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}
	
	@Test
	public void should_not_repeat_tags_even_if_different_case() throws Exception {
		List<Tag> found = tags.findAllDistinct(asList("Java", "java", "ruBy", "ruby"));
		assertEquals(2, found.size());
		assertEquals("java", found.get(0).getName());
		assertEquals("ruby", found.get(1).getName());
	}
	
	@Test
	public void should_not_save_repeated_tags() throws Exception {
		int originalSize = tags.all().size();
		Tag csharp = new Tag("csharp", "", leo);
		tags.saveIfDoesntExists(csharp);
		tags.saveIfDoesntExists(csharp);

		assertEquals(tags.all().size(), originalSize+1);
	}

	private Question questionWith(List<Tag> tags) {
		Question question = new QuestionBuilder().withAuthor(leo).withTags(tags).build();
		session.save(question);
		return question;
	}

	@After
	public void tearDown() {
		DateTimeUtils.setCurrentMillisSystem();
	}

	
}

