package org.learn.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.learn.brutauth.auth.rules.EditAnswerRule;
import org.learn.brutauth.auth.rules.EnvironmentKarma;
import org.learn.dao.TestCase;
import org.learn.model.Answer;
import org.learn.model.LoggedUser;
import org.learn.vraptor.environment.LearnEnvironment;

import javax.servlet.ServletContext;
import java.io.IOException;

public class EditAnswerRuleTest extends TestCase{
	private static final long NOT_AUTHOR = 2l;
	private static final Long AUTHOR_ID = 1l;
	private LoggedUser author;
	private Answer answer;

	@Before
	public void setUp(){
		author = loggedUser("author", "author@brutal.com", AUTHOR_ID);
		answer = answer(null, question(author.getCurrent()), author.getCurrent());
	}

	@Test
	public void author_should_be_allowed_to_edit_moderatable() throws IOException {
		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new LearnEnvironment(ctx));
		assertTrue(new EditAnswerRule(author, env).isAllowed(answer));
	}
	
	@Test
	public void loggedUser_with_enough_karma_should_be_allowed_to_edit() throws IOException {
		LoggedUser loggedUserWithEnoughKarma = loggedUser("loggedUser", "loggedUser@brutal.com", NOT_AUTHOR);
		loggedUserWithEnoughKarma.getCurrent().increaseKarma(20);
		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new LearnEnvironment(ctx));
		
		assertTrue(new EditAnswerRule(loggedUserWithEnoughKarma, env).isAllowed(answer));
	}
	
	@Test
	public void moderator_should_be_allowed_to_edit() throws IOException {
		LoggedUser loggedUser = loggedUser("moderator", "moderator@brutal.com", NOT_AUTHOR);
		loggedUser.getCurrent().asModerator();

		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new LearnEnvironment(ctx));
		assertTrue(new EditAnswerRule(loggedUser, env).isAllowed(answer));
	}
	
	@Test
	public void loggedUser_with_low_karma_should_not_be_allowed_to_edit() throws IOException {
		LoggedUser other = loggedUser("other", "other@brutal.com", NOT_AUTHOR);

		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new LearnEnvironment(ctx));
		assertFalse(new EditAnswerRule(other, env).isAllowed(answer));
	}

}
