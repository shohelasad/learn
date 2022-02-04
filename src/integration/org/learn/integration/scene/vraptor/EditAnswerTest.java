package org.learn.integration.scene.vraptor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.learn.model.Answer;
import org.learn.model.AnswerAndVotes;
import org.learn.model.Question;
import org.learn.model.User;

import br.com.caelum.vraptor.test.VRaptorTestResult;
import br.com.caelum.vraptor.test.requestflow.UserFlow;

public class EditAnswerTest extends CustomVRaptorIntegration {

	@Test
	public void should_edit_answer_of_other_author() throws Exception {
		User moderator = moderator();
		Question question = createQuestionWithDao(moderator,
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		Answer answer = answerQuestionWithDao(moderator, question,
				"Resposta da questao do teste de edicao", false);

		String newDescription = "my new description of the first answer";

		UserFlow navigation = login(navigate(), karmaNigga().getEmail());
		navigation = editAnswerWithFlow(navigation, answer, newDescription,
				"comment");

		VRaptorTestResult answerEdited = navigation.followRedirect().execute();
		answerEdited.wasStatus(200).isValid();

		List<String> messagesList = messagesList(answerEdited);
		assertTrue(messagesList.contains(message("status.pending")));
	}

	@Test
	public void should_edit_and_automatically_approve_author_edit()
			throws Exception {
		Question question = createQuestionWithDao(moderator(),
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		User karmaNigga = karmaNigga();
		Answer answer = answerQuestionWithDao(karmaNigga, question,
				"Resposta da questao do teste de edicao", false);

		String newDescription = "my new description of the first answer";

		UserFlow navigation = login(navigate(), karmaNigga.getEmail());
		navigation = editAnswerWithFlow(navigation, answer, newDescription,
				"comment");

		VRaptorTestResult answerEdited = navigation.followRedirect().execute();
		answerEdited.wasStatus(200).isValid();

		List<String> messagesList = messagesList(answerEdited);
		assertTrue(messagesList.contains(message("status.no_need_to_approve")));

		AnswerAndVotes answerAndVotes = answerEdited.getObject("answers");
		List<Answer> answers = new ArrayList<Answer>(answerAndVotes.getVotes().keySet());
		assertEquals(newDescription, answers.get(0).getDescription());
	}

	@Test
    public void should_edit_and_automatically_approve_moderator() throws Exception {
		User moderator = moderator();
		Question question = createQuestionWithDao(moderator,
				"Titulo da questao hahaha",
				"Descricao da questao longa demais", tag("java"));

		Answer answer = answerQuestionWithDao(karmaNigga(), question,
				"Resposta da questao do teste de edicao", false);

		String newDescription = "my new description of the first answer";

		UserFlow navigation = login(navigate(), moderator.getEmail());
		navigation = editAnswerWithFlow(navigation, answer, newDescription,
				"comment");

		VRaptorTestResult answerEdited = navigation.followRedirect().execute();
		answerEdited.wasStatus(200).isValid();

		List<String> messagesList = messagesList(answerEdited);
		assertTrue(messagesList.contains(message("status.no_need_to_approve")));

		AnswerAndVotes answerAndVotes = answerEdited.getObject("answers");
		List<Answer> answers = new ArrayList<Answer>(answerAndVotes.getVotes().keySet());
		assertEquals(newDescription, answers.get(0).getDescription());
    }

}
