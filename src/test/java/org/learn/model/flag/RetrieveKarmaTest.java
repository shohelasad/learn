package org.learn.model.flag;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.learn.dao.TestCase;
import org.learn.model.Answer;
import org.learn.model.Comment;
import org.learn.model.LoggedUser;
import org.learn.model.Question;
import org.learn.model.User;
import org.learn.model.flag.RetrieveKarma;

public class RetrieveKarmaTest extends TestCase{

	private RetrieveKarma retrieveKarma;
	private Question question;
	private RetrieveKarma retrieveKarmaWithoutModerator;

	@Before
	public void setUp () {
		User moderator = user(null, null).asModerator();
		User user = user(null, null);
		retrieveKarma = new RetrieveKarma(new LoggedUser(moderator, null), null, null);
		retrieveKarmaWithoutModerator = new RetrieveKarma(new LoggedUser(user, null), null, null);
		question = question(null);
	}
	
	@Test
	public void should_handle_question_with_moderator() {
		assertTrue(retrieveKarma.shouldHandle(question));
	}
	
	@Test
	public void should_handle_answer_with_moderator() {
		Answer answer= answer(null, question, null);
		assertTrue(retrieveKarma.shouldHandle(answer));
	}
	
	@Test
	public void should_not_handle_question_without_moderator() {
		assertFalse(retrieveKarmaWithoutModerator.shouldHandle(question));
	}
	
	@Test
	public void should_not_handle_answer_without_moderator() {
		Answer answer= answer(null, question, null);
		assertFalse(retrieveKarmaWithoutModerator.shouldHandle(answer));
	}
	
	@Test
	public void should_not_handle_comment() {
		Comment comment = comment(null, null);
		assertFalse(retrieveKarma.shouldHandle(comment));
	}

}
