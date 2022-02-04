package org.learn.mail;

import br.com.caelum.vraptor.simplemail.AsyncMailer;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.simplemail.template.TemplateMail;
import br.com.caelum.vraptor.simplemail.template.TemplateMailer;
import org.apache.commons.mail.Email;
import org.junit.Before;
import org.junit.Test;
import org.learn.controllers.QuestionController;
import org.learn.mail.NewQuestionMailer;
import org.learn.model.*;
import org.learn.vraptor.Linker;
import org.mockito.internal.verification.VerificationModeFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class NewQuestionMailerTest {

	private NewQuestionMailer questionMailer;
	private AsyncMailer mailer;
	private HttpServletRequest request;

	@Before
	public void setup() {
		this.mailer = mock(AsyncMailer.class);
		TemplateMailer templates = mock(TemplateMailer.class);
		BundleFormatter bundle = mock(BundleFormatter.class);
		Linker linker = mock(Linker.class);
		QuestionController questionController = mock(QuestionController.class);
		this.request = mock(HttpServletRequest.class);
		TemplateMail mail = mock(TemplateMail.class);

		this.questionMailer = new NewQuestionMailer(mailer, templates, bundle, linker, "logo");

		when(request.getRemoteAddr()).thenReturn("127.0.0.1");
		when(linker.linkTo(QuestionController.class)).thenReturn(questionController);
		when(templates.template(anyString())).thenReturn(mail);
		when(mail.with(anyString(), any())).thenReturn(mail);
	}

	@Test
	public void should_not_send_to_question_author() {
		User author = new User(SanitizedText.fromTrustedText("author"), "author@jiggasa.com.bd");
		List<User> users = asList(author);
		QuestionInformation info = new QuestionInformation("title", MarkedText.notMarked("content"), new LoggedUser(author, request));
		Question question = new Question(info, author);
		questionMailer.send(users, question);

		verify(mailer, never()).asyncSend(any(Email.class));
	}

	@Test
	public void should_send_to_subscribed_user() {
		User author = new User(SanitizedText.fromTrustedText("author"), "author@jiggasa.com.bd");
		author.setId(1l);
		User subscribed = new User(SanitizedText.fromTrustedText("subscribed"), "subscribed@jiggasa.com.bd");
		subscribed.setId(2l);

		QuestionInformation info = new QuestionInformation("title", MarkedText.notMarked("content"), new LoggedUser(author, request));
		Question question = new Question(info, author);
		questionMailer.send(asList(subscribed), question);

		verify(mailer, times(1)).asyncSend(any(Email.class));
	}



}