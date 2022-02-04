package org.learn.controllers;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.validator.I18nMessage;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import org.learn.auth.FacebookAuthService;
import org.learn.brutauth.auth.rules.*;
import org.learn.dao.*;
import org.learn.event.QuestionCreated;
import org.learn.factory.MessageFactory;
import org.learn.filesystem.AttachmentRepository;
import org.learn.interceptors.IncludeAllTags;
import org.learn.managers.TagsManager;
import org.learn.model.*;
import org.learn.model.post.PostViewCounter;
import org.learn.model.watch.Watcher;
import org.learn.search.QuestionIndex;
import org.learn.util.TagsSplitter;
import org.learn.validators.AttachmentsValidator;
import org.learn.validators.TagsValidator;
import org.learn.vraptor.Linker;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static br.com.caelum.vraptor.view.Results.http;
import static java.util.Arrays.asList;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

@Routed
@Controller
public class QuestionController {

	@Inject
	private Result result;
	@Inject
	private QuestionDAO questionDao;
	@Inject
	private ChannelDAO channelDao;
	@Inject
	private VoteDAO votes;
	@Inject
	private LoggedUser currentUser;
	@Inject
	private TagsValidator tagsValidator;
	@Inject
	private MessageFactory messageFactory;
	@Inject
	private Validator validator;
	@Inject
	private FacebookAuthService facebook;
	@Inject
	private PostViewCounter viewCounter;
	@Inject
	private Linker linker;
	@Inject
	private WatcherDAO watchers;
	@Inject
	private ReputationEventDAO reputationEvents;
	@Inject
	private BrutalValidator brutalValidator;
	@Inject
	private TagsManager tagsManager;
	@Inject
	private TagsSplitter splitter;
	@Inject
	private AttachmentDao attachments;
	@Inject
	private AttachmentsValidator attachmentsValidator;
	@Inject
	private Environment environment;
	@Inject
	private QuestionIndex index;
	@Inject
	private Event<QuestionCreated> questionCreated;
	@Inject
	private AttachmentRepository attachmentRepository;
	@Inject
	private EnvironmentKarma environmentKarma;
	@Inject 
	private TagDictionaryDAO tags;

	@Get
	@IncludeAllTags
	@CustomBrutauthRules(LoggedRule.class)
	@Path(priority=Path.HIGH, value="")
	public void questionForm() {
	}

	@Get
	@IncludeAllTags
	@CustomBrutauthRules(EditQuestionRule.class)
	public void questionEditForm(@Load Question question) {
		result.include("question",  question);
	}

	@Post
	@CustomBrutauthRules(EditQuestionRule.class)
	public void edit(@Load Question original, String title, MarkedText description, String tagNames,
			String comment, List<Long> attachmentsIds) {

		List<String> splitedTags = splitter.splitTags(tagNames);
		List<Tag> loadedTags = tagsManager.findOrCreate(splitedTags);
		//comment out for unicode bangla support
		//validate(loadedTags, splitedTags);
		comment = "edit comment";
		
		QuestionInformation information = original.getInformation();
		if(information.getAuthor().getId().equals(currentUser.getCurrent().getId())) {
			information.setComment(comment);
			information.setTitle(title);
			information.setTags(loadedTags);
			information.setDescription(description);
			information.setIp(currentUser.getIp());
		} else {		
		    information = new QuestionInformation(title, description, this.currentUser, loadedTags, comment);
		}
		
		brutalValidator.validate(information);
		UpdateStatus status = original.updateWith(information, new Updater(environmentKarma));

		validator.onErrorUse(Results.page()).of(this.getClass()).questionEditForm(original);

		result.include("editComment", comment);
		result.include("question", original);

		questionDao.save(original);
		List<Attachment> attachmentsLoaded = attachments.load(attachmentsIds);
		original.removeAttachments();
		original.add(attachmentsLoaded);
		index.indexQuestion(original);

		result.include("learnMessages",
				Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		result.redirectTo(this).showQuestion(original, original.getSluggedTitle());
	}

	@Get
	public void showQuestion(@Load Question question, String sluggedTitle){
		User current = currentUser.getCurrent();
		if (question.isVisibleFor(current)){
			result.include("markAsSolution", question.canMarkAsSolution(current));
			result.include("showUpvoteBanner", !current.isVotingEnough());
			result.include("editedLink", true);

			redirectToRightUrl(question, sluggedTitle);
			viewCounter.ping(question);
			boolean isWatching = watchers.ping(question, current);
			result.include("currentVote", votes.previousVoteFor(question.getId(), current, Question.class));
			result.include("answers", votes.previousVotesForAnswers(question, current));
			result.include("commentsWithVotes", votes.previousVotesForComments(question, current));
			result.include("questionTags", question.getTags());
			result.include("recentQuestionTags", question.getTagsUsage());
			result.include("relatedQuestions", questionDao.getRelatedTo(question));
			result.include("question", question);
			result.include("isWatching", isWatching);
			result.include("userMediumPhoto", true);
			linker.linkTo(this).showQuestion(question, sluggedTitle);
			result.include("facebookUrl", facebook.getOauthUrl(linker.get()));
			result.include("isHomePage", true);
			//result.include("isTagVisible", true);
			result.include("relatedChannels", channelDao.getRelatedChannelsByQuestion(question, current));
			
		} else {
			result.notFound();
		}
		
		result.include("featuredQuestions", questionDao.getRecentQuestions("/"));	
	}

	@Post
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newQuestion(String title, MarkedText description, String tagNames, boolean watching,
							List<Long> attachmentsIds) {
		List<Attachment> attachments = this.attachments.load(attachmentsIds);
		attachmentsValidator.validate(attachments);
		
		
		//TODO: search title for finding tags
		String[] tokens = title.split(" ");
		tagNames = "";
		for(int i = 0; i < tokens.length; i++) {
			if(tags.getTags(tokens[i]) != null)
				tagNames += tags.getTags(tokens[i]) + ",";
		}
		
		List<String> splitedTags = splitter.splitTags(tagNames);

		List<Tag> foundTags = tagsManager.findOrCreate(splitedTags);
		validate(foundTags, splitedTags);

		QuestionInformation information = new QuestionInformation(title, description, currentUser, foundTags, "new question");
		brutalValidator.validate(information);
		User author = currentUser.getCurrent();
		//information.setInitStatus(UpdateStatus.PENDING);
		Question question = new Question(information, author);
		result.include("question", question);
		validator.onErrorUse(Results.page()).of(this.getClass()).questionForm();

		questionDao.save(question);
		index.indexQuestion(question);
		question.add(attachments);

		ReputationEvent reputationEvent = new ReputationEvent(EventType.CREATED_QUESTION, question, author);
		author.increaseKarma(reputationEvent.getKarmaReward());
		reputationEvents.save(reputationEvent);
		if (watching) {
			watchers.add(question, new Watcher(author));
		}

		questionCreated.fire(new QuestionCreated(question));
		result.include("learnMessages", asList(messageFactory.build("alert", "question.quality_reminder")));
		//result.redirectTo(this).showQuestion(question, question.getSluggedTitle());
		result.redirectTo(this).questionEditForm(question);

	}

	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Get
	public void showVoteInformation (@Load Question question, String sluggedTitle){
		result.include("question", question);
		redirectToRightUrl(question, sluggedTitle);
	}

	@Delete
	public void deleteQuestion(@Load Question question) {
		if (!environment.supports("deletable.questions")) {
			result.notFound();
			return;
		}
		if (!currentUser.isModerator() && !question.hasAuthor(currentUser.getCurrent())) {
			result.use(http()).sendError(403);
			return;
		}

		I18nMessage errorMessage = messageFactory.build("error", "question.errors.deletion");
		if (!question.isDeletable()) {
			result.use(http())
				.body(errorMessage.getMessage())
				.setStatusCode(400);
			return;
		}
		result.include("learnMessages", asList(messageFactory.build("confirmation", "question.delete.confirmation")));
		attachmentRepository.delete(question.getAttachments());

		index.delete(question);
		questionDao.delete(question);

		result.redirectTo(ListController.class).home(null);
	}

	@CustomBrutauthRules({ModeratorOnlyRule.class})
	@Delete
	public void deleteQuestionFully(@Load Question question) {
		if (!environment.supports("deletable.questions")) {
			result.notFound();
			return;
		}
		Iterable<Attachment> attachments = question.getAllAttachments();
		this.attachmentRepository.delete(attachments);
		questionDao.deleteFully(question, currentUser.getCurrent());
		index.delete(question);

		result.include("learnMessages", asList(messageFactory.build("confirmation", "question.delete.confirmation")));
		result.redirectTo(ListController.class).home(null);
	}

	private boolean validate(List<Tag> foundTags, List<String> splitedTags) {
		return tagsValidator.validate(foundTags, splitedTags);
	}

	private void redirectToRightUrl(Question question, String sluggedTitle) {
		if (!question.getSluggedTitle().equals(sluggedTitle)) {
			result.redirectTo(this).showQuestion(question,
					question.getSluggedTitle());
			return;
		}
	}
}
