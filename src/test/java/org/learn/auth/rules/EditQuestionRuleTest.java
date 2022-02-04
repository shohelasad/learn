package org.learn.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;
import org.learn.brutauth.auth.rules.EditQuestionRule;
import org.learn.brutauth.auth.rules.EnvironmentKarma;
import org.learn.builder.QuestionBuilder;
import org.learn.dao.TestCase;
import org.learn.model.LoggedUser;
import org.learn.model.Question;
import org.learn.vraptor.environment.LearnEnvironment;

import javax.servlet.ServletContext;
import java.io.IOException;

public class EditQuestionRuleTest extends TestCase {

	private Question question;
	private LoggedUser author;

	@Before
	public void setUp(){
		author = loggedUser("author", "author@brutal.com", 1l);
		question = new QuestionBuilder().withAuthor(author.getCurrent()).build();
	}

	@Test
	public void author_should_be_allowed_to_edit_moderatable() throws IOException {
		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new LearnEnvironment(ctx));
		assertTrue(new EditQuestionRule(author, env).isAllowed(question));
	}
	
	@Test
	public void user_with_enough_karma_should_be_allowed_to_edit() throws IOException {
		LoggedUser userWithEnoughKarma = loggedUser("user", "user@brutal.com", 2l);
		userWithEnoughKarma.getCurrent().increaseKarma(20);

		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new LearnEnvironment(ctx));
		assertTrue(new EditQuestionRule(userWithEnoughKarma, env).isAllowed(question));
	}



	@Test
	public void moderator_should_be_allowed_to_edit() throws IOException {
		LoggedUser moderator = loggedUser("moderator", "moderator@brutal.com", 2l);
		moderator.getCurrent().asModerator();
		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new LearnEnvironment(ctx));
		assertTrue(new EditQuestionRule(moderator, env).isAllowed(question));
	}
	
	@Test
	public void user_with_low_karma_should_not_be_allowed_to_edit() throws IOException {
		LoggedUser other = loggedUser("other", "other@brutal.com", 3l);
		ServletContext ctx = mock(ServletContext.class);
		EnvironmentKarma env = new EnvironmentKarma(new LearnEnvironment(ctx));
		assertFalse(new EditQuestionRule(other, env).isAllowed(question));
	}

}
