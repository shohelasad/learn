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

import org.learn.brutauth.auth.rules.*;
import org.learn.components.RecentTagsContainer;
import org.learn.dao.*;
import org.learn.event.ChannelCreated;
import org.learn.event.PageCreated;
import org.learn.event.QuestionCreated;
import org.learn.factory.MessageFactory;
import org.learn.filesystem.AttachmentRepository;
import org.learn.interceptors.IncludeAllTags;
import org.learn.managers.TagsManager;
import org.learn.model.*;
import org.learn.model.watch.Watcher;
import org.learn.util.TagsSplitter;
import org.learn.validators.AttachmentsValidator;
import org.learn.validators.TagsValidator;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static br.com.caelum.vraptor.view.Results.http;
import static java.util.Arrays.asList;


@Routed
@Controller
public class ChannelController {

	@Inject
	private Result result;
	@Inject 
	private RecentTagsContainer recentTagsContainer;
	@Inject
	private ChannelDAO channelDao;
	@Inject
	private TagDAO tagDao;
	@Inject
	private QuestionDAO questionDao;
	@Inject
	private LoggedUser currentUser;
	@Inject
	private TagsValidator tagsValidator;
	@Inject
	private MessageFactory messageFactory;
	@Inject
	private Validator validator;
	@Inject
	private BrutalValidator brutalValidator;
	@Inject
	private TagsManager tagsManager;
	@Inject
	private TagsSplitter splitter;
	@Inject
	private Environment environment;
	@Inject
	private EnvironmentKarma environmentKarma;
	@Inject
	private AttachmentRepository attachmentRepository;
	@Inject
	private AttachmentDao attachments;
	@Inject
	private AttachmentsValidator attachmentsValidator;
	@Inject
	private Event<ChannelCreated> channelCreated;
	@Inject
	private WatcherDAO watchers;

	@Get
	@IncludeAllTags
	@CustomBrutauthRules(LoggedRule.class)
	public void channelForm() {
	
	}
	
	@Post
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newChannel(String title, String url, MarkedText description, String tagNames, List<Long> attachmentsIds) {
		
		List<Attachment> attachments = this.attachments.load(attachmentsIds);
		attachmentsValidator.validate(attachments);
		
		List<String> splitedTags = splitter.splitTags(tagNames);

		List<Tag> foundTags = tagsManager.findOrCreate(splitedTags);
		validate(foundTags, splitedTags);

		ChannelInformation information = new ChannelInformation(title, url, description, currentUser, foundTags, "new channel");
		//Page page = new Page(information, currentUser, parent);
		brutalValidator.validate(information);
		
		User author = currentUser.getCurrent();
		//information.setInitStatus(UpdateStatus.PENDING);
		Channel channel = new Channel(information, author);
		channel.setParent(channel);
		result.include("channel", channel);
		validator.onErrorUse(Results.page()).of(this.getClass()).channelForm();
		channel.add(attachments);

		channelDao.save(channel);
		
		channelCreated.fire(new ChannelCreated(channel));
		result.include("learnMessages", asList(messageFactory.build("alert", "question.quality_reminder")));
		result.redirectTo(this).channels(1);
	}
	
	@Get
	@IncludeAllTags
	@CustomBrutauthRules(LoggedRule.class)
	public void channelPageForm() {
		List<Channel> channels = channelDao.findChannels();
		System.out.println("Channels size: " + channels.size());
		result.include("channels", channels);
	}
	
	@Post
	@CustomBrutauthRules({LoggedRule.class, InputRule.class})
	public void newChannelPage(Long parentId, String title, String url, MarkedText description, String tagNames, List<Long> attachmentsIds) {
		
		List<Attachment> attachments = this.attachments.load(attachmentsIds);
		attachmentsValidator.validate(attachments);
		
		List<String> splitedTags = splitter.splitTags(tagNames);

		List<Tag> foundTags = tagsManager.findOrCreate(splitedTags);
		validate(foundTags, splitedTags);

		ChannelInformation information = new ChannelInformation(title, url, description, currentUser, foundTags, "new channel");
		//Page page = new Page(information, currentUser, parent);
		brutalValidator.validate(information);
		
		User author = currentUser.getCurrent();
		//information.setInitStatus(UpdateStatus.PENDING);
		Channel channel = new Channel(information, author);
		Channel parent = channelDao.getById(parentId);
		channel.setParent(parent);
		result.include("channel", channel);
		validator.onErrorUse(Results.page()).of(this.getClass()).channelForm();
		channel.add(attachments);

		channelDao.save(channel);
		
		channelCreated.fire(new ChannelCreated(channel));
		result.include("learnMessages", asList(messageFactory.build("alert", "question.quality_reminder")));
		result.redirectTo(this).channels(1);
	}

	@Get
	@IncludeAllTags    
	@CustomBrutauthRules(EditChannelRule.class)
	public void channelEditForm(@Load Channel channel) {
		result.include("channel",  channel);
	}

	/*@Post
	@CustomBrutauthRules(EditChannelRule.class)
	public void edit(@Load Channel original, String title, MarkedText description, String tagNames,
			String comment, List<Long> attachmentsIds) {

		List<String> splitedTags = splitter.splitTags(tagNames);
		List<Tag> loadedTags = tagsManager.findOrCreate(splitedTags);

		validator.onErrorUse(Results.page()).of(this.getClass()).channelEditForm(original);

		result.include("page", original);

		channelDao.save(original);

		result.include("learnMessages",
				Arrays.asList(messageFactory.build("confirmation", "Channel Created")));
		result.redirectTo(this).channels(1);
	}*/
	
	@Post
	@CustomBrutauthRules(EditChannelRule.class)
	public void edit(@Load Channel original, String title, String url, MarkedText description, String tagNames,
			String comment, List<Long> attachmentsIds) {

		List<String> splitedTags = splitter.splitTags(tagNames);
		List<Tag> loadedTags = tagsManager.findOrCreate(splitedTags);
		//comment out for unicode bangla support
		//validate(loadedTags, splitedTags);
		comment = "edit channel";
		ChannelInformation information = original.getInformation(); //new ChannelInformation(title, original.getInformation().getUrl(), description, currentUser, loadedTags, "new channel");
		information.setComment(comment);
		information.setTitle(title);
		information.setTags(loadedTags);
		information.setDescription(description);
		information.setIp(currentUser.getIp());
		//information.setModeratar(currentUser.getCurrent());
		brutalValidator.validate(information);
		UpdateStatus status = original.updateWith(information, new Updater(environmentKarma));

		validator.onErrorUse(Results.page()).of(this.getClass()).channelEditForm(original);

		result.include("editComment", comment);
		result.include("page", original);

		channelDao.save(original);
		List<Attachment> attachmentsLoaded = attachments.load(attachmentsIds);
		original.removeAttachments();
		original.add(attachmentsLoaded);
		//index.indexChannel(original);

		result.include("learnMessages",
				Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		result.redirectTo(this).channels(1);
	}

	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void channels(Integer p) {
		Integer page = getPage(p);
		List<Channel> channels = channelDao.findChannels(page, false);
		result.include("channels", channels);
	}
	
	private Integer getPage(Integer p) {
		Integer page = p == null ? 1 : p;
		return page;
	}

	/*@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Get
	public void showVoteInformation (@Load Channel channel, String name){
		result.include("channel", channel);
		redirectToRightUrl(channel, name);
	}

	@Delete
	public void deleteChannel(@Load Question question) {
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

		channelDao.delete(question);

		result.redirectTo(ListController.class).home(null);
	}
	*/

	private boolean validate(List<Tag> foundTags, List<String> splitedTags) {
		return tagsValidator.validate(foundTags, splitedTags);
	}

	/*private void redirectToRightUrl(Channel channel, String name) {
		if (!channel.getName().equals(name)) {
			result.redirectTo(this).showChannel(channel, name);
			return;
		}
	}*/
}
