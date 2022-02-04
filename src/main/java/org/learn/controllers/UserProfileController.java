package org.learn.controllers;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.observer.upload.UploadedFile;
import br.com.caelum.vraptor.routes.annotation.Routed;

import org.joda.time.DateTime;
import org.learn.brutauth.auth.rules.LoggedRule;
import org.learn.brutauth.auth.rules.ModeratorOnlyRule;
import org.learn.dao.*;
import org.learn.dao.WithUserPaginatedDAO.OrderType;
import org.learn.dto.UserPersonalInfo;
import org.learn.factory.MessageFactory;
import org.learn.filesystem.AttachmentsFileStorage;
import org.learn.filesystem.ImageStore;
import org.learn.infra.ClientIp;
import org.learn.model.*;
import org.learn.validators.UserPersonalInfoValidator;

import javax.inject.Inject;
import java.io.IOException;

import static br.com.caelum.vraptor.view.Results.http;
import static br.com.caelum.vraptor.view.Results.json;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.learn.dao.WithUserPaginatedDAO.OrderType.ByDate;
import static org.learn.dao.WithUserPaginatedDAO.OrderType.ByVotes;
import static org.learn.model.SanitizedText.fromTrustedText;

@Routed
@Controller
public class UserProfileController extends BaseController{
	
	private static final String HTTP = "http://";
	@Inject private Result result;
	@Inject private UserDAO users;
	@Inject private LoggedUser currentUser;
	@Inject private UserPersonalInfoValidator infoValidator;
	@Inject private QuestionDAO questions;
	@Inject private AnswerDAO answers;
    @Inject private TagDAO tags;
	@Inject private WatcherDAO watchers;
	@Inject private ReputationEventDAO reputationEvents;
	@Inject private MessageFactory messageFactory;
	@Inject private FlaggableDAO flaggable;
	@Inject private ClientIp clientIp;
	@Inject private AttachmentDao attachments;
	@Inject private AttachmentsFileStorage fileStorage;
	@Inject private ImageStore imageStore;
    @Inject private Environment environment;
    @Inject private CommentDAO comments;

	@Get
	public void showProfile(@Load User user, String sluggedName){
		if (redirectToRightSluggedName(user, sluggedName)) {
			return;
		}
		
		result.include("isCurrentUser", currentUser.getCurrent().getId().equals(user.getId()));
		result.include("questionsByVotes", questions.ofUserPaginatedBy(user, ByVotes, 1));
		result.include("questionsCount", questions.countWithAuthor(user));
		result.include("answersByVotes", answers.ofUserPaginatedBy(user, ByVotes, 1));
		result.include("answersCount", answers.countWithAuthor(user));
		result.include("watchedQuestions", watchers.ofUserPaginatedBy(user, ByDate, 1));
		result.include("watchedQuestionsCount", watchers.countWithAuthor(user));
		
		result.include("reputationHistory", reputationEvents.karmaWonByQuestion(user, new DateTime().minusMonths(1), 5).getHistory());
		
		result.include("questionsPageTotal", questions.numberOfPagesTo(user));
		result.include("answersPageTotal", answers.numberOfPagesTo(user));
		result.include("watchedQuestionsPageTotal", watchers.numberOfPagesTo(user));
		
		result.include("userProfileMainTags", tags.findMainTagsOfUser(user));
		result.include("selectedUser", user);
		result.include("usersActive", true);
		result.include("noDefaultActive", true);
		
		User current = currentUser.getCurrent();
		boolean isFollowing = watchers.ping(user, current);
		result.include("isFollowing", isFollowing);
		result.include("isProfilePage", true);
		result.include("isHomeMenu", false);
		
	}
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void reputationHistory(@Load User user, String sluggedName) {
		if (redirectToRightSluggedName(user, sluggedName)) {
			return;
		}
		result.include("selectedUser", user);
		result.include("reputationHistory", reputationEvents.karmaWonByQuestion(user).getHistory());
		result.include("usersActive", true);
		result.include("noDefaultActive", true);
		result.include("isProfilePage", true);
	}

	@Get
	public void typeByVotesWith(Long id, String sluggedName, OrderType order, Integer p, String type){
		User author = users.findById(id);
		order = order == null ? ByVotes : order;
		Integer page = p == null ? 1 : p;
		PaginatableDAO paginatableDAO = type.equals(i18n("metas", "metas.questions_lowercase").getMessage()) ? questions : answers;
		result.forwardTo(BrutalTemplatesController.class).userProfilePagination(paginatableDAO, author, order, page, type);		
	}
	
	@Get
	public void watchersByDateWith(Long id, String sluggedName, Integer p){
		User user = users.findById(id);
		Integer page = p == null ? 1 : p;
		result.forwardTo(BrutalTemplatesController.class).userProfilePagination(watchers, user, ByDate, page, i18n("metas", "metas.watched_lowercase").getMessage());
	}
		
	@Get
	public void editProfile(@Load User user){
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home(null);
			return;
		}
		result.include("user", user);
		result.include("usersActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Post
	public void editProfile(@Load User user, SanitizedText name, String email, 
			SanitizedText website, SanitizedText location, SanitizedText occupation, SanitizedText organization, DateTime birthDate, MarkedText description,
			boolean isSubscribed, boolean receiveAllUpdates) {
		if (!user.getId().equals(currentUser.getCurrent().getId())){
			result.redirectTo(ListController.class).home(null);
			return;
		}
		
		if (website != null) {
			website = correctWebsite(website);
		}

		UserPersonalInfo info = new UserPersonalInfo(user)
			.withName(name)
			.withEmail(email)
			.withWebsite(website)
			.withLocation(location)
			.withOccupation(occupation)
			.withOrganization(organization)
			.withBirthDate(birthDate)
			.withAbout(description)
			.withReceiveAllUpdates(receiveAllUpdates)
			.withIsSubscribed(isSubscribed);
		
		if (!infoValidator.validate(info)) {
			infoValidator.onErrorRedirectTo(this).editProfile(user);
			return;
		}
		
		users.updateLoginMethod(user, email);
		
		user.setPersonalInformation(info);
		
		result.redirectTo(this).showProfile(user, user.getSluggedName());
	}
	
	@Get
	public void unsubscribe(@Load User user, String hash){
		String correctHash = user.getUnsubscribeHash();
		
		if (!correctHash.equals(hash)) {
			result.include("learnMessages", asList(messageFactory.build("errors", "newsletter.unsubscribe_page.invalid")));
			result.redirectTo(ListController.class).home(null);
			return;
		}
		result.include("learnMessages", asList(messageFactory.build("messages", "newsletter.unsubscribe_page.valid")));
		user.setSubscribed(false);
		result.redirectTo(ListController.class).home(null);
	}
	
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Post
	public void toogleBanned(@Load User user) {
		if(user.isBanned()){
			user.undoBan();
		}else{
			user.ban();
			flaggable.turnAllInvisibleWith(user);
			users.clearSessionOf(user);
		}
		result.nothing();
	}

	@CustomBrutauthRules(LoggedRule.class)
	@Post
	public void uploadAvatar(UploadedFile avatar, @Load User user) {
		Attachment attachment = null;
		try {
			attachment = imageStore.processAndStore(avatar, user, clientIp);
		} catch (IOException e) {
			result.use(http()).sendError(400);
			return;
		}

		Attachment old = user.getAvatar();
		if (old != null) {
			imageStore.delete(old);
		}

		user.setAvatar(attachment);
		result.use(json()).withoutRoot().from(attachment).serialize();
	}

	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Delete
	public void delete(Long userId) {
		if (!this.environment.supports("deletable.users")) {
			result.notFound();
			return;
		}
		User user = users.findById(userId);
		questions.deleteQuestionsOf(user);
		answers.deleteAnswersOf(user);
		comments.deleteCommentsOf(user);
		users.delete(user);

		result.include("learnMessages", asList(messageFactory.build("confirmation", "user.delete.confirmation")));
		result.redirectTo(ListController.class).home(null);
	}

	private SanitizedText correctWebsite(SanitizedText website) {
		String text = website.getText();
		if (isBlank(text))
			return fromTrustedText("");
		if (text.startsWith(HTTP))
			return website;
		return fromTrustedText(HTTP + text);
	}
	
	private boolean redirectToRightSluggedName(User user, String sluggedName) {
		String correctSluggedName = user.getSluggedName();
		if (!correctSluggedName.equals(sluggedName)) {
			result.redirectTo(this).showProfile(user, correctSluggedName);
			return true;
		}
		return false;
	}
}
