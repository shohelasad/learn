package org.learn.auth.rules;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.learn.auth.rules.AuthorRule;
import org.learn.builder.QuestionBuilder;
import org.learn.dao.TestCase;
import org.learn.model.Question;
import org.learn.model.User;
import org.learn.model.interfaces.Moderatable;

public class AuthorRuleTest extends TestCase {

	@Test
	public void should_allow_moderatable_author() {
		AuthorRule<Moderatable> rule = new AuthorRule<>();
		User author = user("author", "author@brutal.com", 1l);
		User other = user("other", "other@brutal.com", 2l);
		Question question = new QuestionBuilder().withAuthor(author).build();
		
		assertFalse(rule.isAllowed(other, question));
		assertTrue(rule.isAllowed(author, question));
	}

}
