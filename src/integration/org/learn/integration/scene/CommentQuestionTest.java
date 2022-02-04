package org.learn.integration.scene;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.learn.integration.util.DaoManager;
import org.learn.model.User;

public class CommentQuestionTest extends AuthenticatedAcceptanceTest {

	@Before
	public void login() {
		DaoManager manager = new DaoManager();

		User author = manager.randomUser();
		manager.createQuestion(author);
	}

	@Test
	public void should_comment_question_after_login() throws Exception {
		this.loginWithALotOfKarma();

		String comment = "my comment my comment my comment";
		home().toFirstQuestionPage()
			.commentQuestion(comment);

		List<String> comments = home()
				.toFirstQuestionPage()
				.questionComments();
		assertTrue(comments.contains(comment));
	}

}
