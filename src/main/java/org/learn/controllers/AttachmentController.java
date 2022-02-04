package org.learn.controllers;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.observer.download.InputStreamDownload;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import org.apache.commons.io.IOUtils;
import org.learn.auth.FacebookAuthService;
import org.learn.brutauth.auth.rules.EditQuestionRule;
import org.learn.brutauth.auth.rules.InputRule;
import org.learn.brutauth.auth.rules.LoggedRule;
import org.learn.brutauth.auth.rules.ModeratorOnlyRule;
import org.learn.dao.*;
import org.learn.factory.MessageFactory;
import org.learn.filesystem.AttachmentRepository;
import org.learn.filesystem.AttachmentsFileStorage;
import org.learn.infra.ClientIp;
import org.learn.interceptors.IncludeAllTags;
import org.learn.managers.TagsManager;
import org.learn.model.*;
import org.learn.model.post.PostViewCounter;
import org.learn.model.watch.Watcher;
import org.learn.search.QuestionIndex;
import org.learn.util.TagsSplitter;
import org.learn.validators.TagsValidator;
import org.learn.vraptor.Linker;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import static java.util.Arrays.asList;

@Routed
@Controller
public class AttachmentController {

	@Inject
	private Result result;
	@Inject
	private QuestionDAO questions;
	@Inject
	private AnswerDAO answers;
	@Inject
	private LoggedUser loggedUser;
	@Inject
	private ClientIp clientIp;
	@Inject
	private HttpServletResponse response;
	@Inject
	private AttachmentRepository attachments;
	@Inject
	private Validator validator;
	@Inject
	private MessageFactory messageFactory;

	@CustomBrutauthRules(LoggedRule.class)
	@Post
	public void uploadAttachment(UploadedFile file) throws IOException {
		Attachment attachment = new Attachment(file, loggedUser.getCurrent(),
				clientIp.get());
		attachments.save(attachment);

		result.use(json()).withoutRoot().from(attachment).serialize();
	}

	@Get
	public InputStreamDownload downloadAttachment(Long attachmentId) throws IOException {
		Attachment attachment = attachments.load(attachmentId);
		InputStream is = attachments.open(attachment);

		return new InputStreamDownload(is, attachment.getMime(), attachment.getName());
	}

	@CustomBrutauthRules(LoggedRule.class)
	@Delete
	public void deleteAttachment(Long attachmentId) throws IOException {
		Attachment attachment = attachments.load(attachmentId);
		User current = loggedUser.getCurrent();
		if (!attachment.canDelete(current) && !current.isModerator()) {
			validator.add(messageFactory.build("error", "unauthorized.title"));
			result.use(http()).sendError(403);
			return;
		}
		attachments.delete(attachment);
		result.nothing();
	}

}
