package org.learn.controllers;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.*;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.hibernate.extra.Load;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;
import br.com.caelum.vraptor.validator.Validator;
import br.com.caelum.vraptor.view.Results;

import org.learn.brutauth.auth.rules.*;
import org.learn.dao.AnswerDAO;
import org.learn.dao.AttachmentDao;
import org.learn.dao.ReputationEventDAO;
import org.learn.dao.WatcherDAO;
import org.learn.factory.MessageFactory;
import org.learn.filesystem.AttachmentRepository;
import org.learn.mail.action.EmailAction;
import org.learn.model.*;
import org.learn.model.watch.Watcher;
import org.learn.notification.NotificationManager;
import org.learn.reputation.rules.KarmaCalculator;
import org.learn.validators.AnsweredByValidator;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static br.com.caelum.vraptor.view.Results.http;

@Routed
@Controller
public class AnswerController {
	@Inject private Result result;
	@Inject private AnswerDAO answers;
	@Inject private LoggedUser currentUser;
    @Inject private KarmaCalculator calculator;
	@Inject private MessageFactory messageFactory;
	@Inject private Validator validator;
	@Inject private AnsweredByValidator answeredByValidator;
	@Inject private NotificationManager notificationManager;
	@Inject private WatcherDAO watchers;
	@Inject private ReputationEventDAO reputationEvents;
	@Inject private BundleFormatter bundle;
	@Inject private BrutalValidator brutalValidator;
	@Inject private AttachmentDao attachments;
	@Inject private EnvironmentKarma environmentKarma;
	@Inject private AttachmentRepository attachmentRepository;
	@Inject private Environment environment;

	@Get
	@CustomBrutauthRules(EditAnswerRule.class)
	public void answerEditForm(@Load Answer answer) {
		result.include("answer",  answer);
	}

	@Post
	@CustomBrutauthRules(EditAnswerRule.class)
	public void edit(@Load Answer original, MarkedText description, String comment, List<Long> attachmentsIds) {
		AnswerInformation information = original.getInformation();
		if(information.getAuthor().getId().equals(currentUser.getCurrent().getId())) {
			information.setComment(comment);
			information.setDescription(description);
			information.setIp(currentUser.getIp());
		} else {
			information = new AnswerInformation(description, currentUser, comment);
		}
		brutalValidator.validate(information);
		
		validator.onErrorRedirectTo(this).answerEditForm(original);
		
		UpdateStatus status = original.updateWith(information, new Updater(environmentKarma));
		answers.save(original);
		List<Attachment> attachmentsLoaded = attachments.load(attachmentsIds);

		original.replace(attachmentsLoaded);

		result.include("learnMessages", Arrays.asList(messageFactory.build("confirmation", status.getMessage())));
		Question originalQuestion = original.getMainThread();
		result.redirectTo(QuestionController.class).showQuestion(originalQuestion, originalQuestion.getSluggedTitle());
	}
	
	@Post
	@CustomBrutauthRules({LoggedRule.class, InputRule.class, InactiveQuestionRequiresMoreKarmaRule.class})
	public void newAnswer(@Load Question question, MarkedText description, boolean watching, List<Long> attachmentsIds) {
		User current = currentUser.getCurrent();
		boolean canAnswer = answeredByValidator.validate(question);
		boolean isUserWithKarma = current.hasKarma();
		AnswerInformation information = new AnswerInformation(description, currentUser, "new answer");
		Answer answer  = new Answer(information, question, current);
		List<Attachment> attachmentsLoaded = attachments.load(attachmentsIds);
		answer.add(attachmentsLoaded);
		if (canAnswer) {
			question.touchedBy(current);
			answers.save(answer);
			ReputationEvent reputationEvent = new ReputationEvent(EventType.CREATED_ANSWER, question, current);
			reputationEvents.save(reputationEvent);

			if (isUserWithKarma) {
				current.increaseKarma(reputationEvent.getKarmaReward());
			}

			result.redirectTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
			notificationManager.sendEmailsAndInactivate(new EmailAction(answer, question));
			if (watching) {
				watchers.add(question, new Watcher(current));
			}
		} else {
			result.include("answer", answer);
			answeredByValidator.onErrorRedirectTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
		}
		
	}
	
	@Post
	public void markAsSolution(Long solutionId) {
		Answer solution = answers.getById(solutionId);
		Question question = solution.getMainThread();
        if (!currentUser.getCurrent().isAuthorOf(question)) {
			result.use(Results.status()).forbidden(bundle.getMessage("answer.error.not_autor"));
			result.redirectTo(QuestionController.class).showQuestion(question,
					question.getSluggedTitle());
			return;
		} 
        markOrRemoveSolution(solution);
        result.nothing();
	}

	@Delete
	public void delete(@Load Answer answer) {
		if (!environment.supports("deletable.answers")) {
			result.notFound();
			return;
		}
		if (!currentUser.isModerator() && !answer.hasAuthor(currentUser.getCurrent())) {
			result.use(http()).sendError(403);
			return;
		}
		if (!answer.isDeletable()) {
			result.use(http())
				.setStatusCode(400);
			return;
		}
		answers.delete(answer);
		attachmentRepository.delete(answer.getAttachments());
		Question question = answer.getQuestion();
		result.redirectTo(QuestionController.class).showQuestion(question, question.getSluggedTitle());
	}

    private void markOrRemoveSolution(Answer answer) {
    	Question question = answer.getMainThread();
    	if (answer.isSolution()) {
    		decreaseKarmaOfOldSolution(answer);
    		answer.uncheckAsSolution();
    		return;
    	} 
		if (question.isSolved()) {
			decreaseKarmaOfOldSolution(question.getSolution());
		}
		answer.markAsSolution();
		increaseKarmaOfUsersInvolved(answer);
	}

    
    private void decreaseKarmaOfOldSolution(Answer solution) {
    	if (!solution.isTheSameAuthorOfQuestion()) {
	    	ReputationEvent solvedQuestion = solvedQuestionEvent(solution);
	    	int karmaForSolutionAuthor = calculator.karmaFor(solvedQuestion);
	    	
	    	ReputationEvent markedSolution = markedSolutionEvent(solution);
	    	int karmaForQuestionAuthorSolution = calculator.karmaFor(markedSolution);
	    	
	    	reputationEvents.delete(solvedQuestion);
	    	reputationEvents.delete(markedSolution);
	    	
	    	solution.getAuthor().descreaseKarma(karmaForSolutionAuthor);
	    	solution.getMainThread().getAuthor().descreaseKarma(karmaForQuestionAuthorSolution);
    	}
    }

    private void increaseKarmaOfUsersInvolved(Answer solution) {
    	if (!solution.isTheSameAuthorOfQuestion()) {
	    	ReputationEvent solvedQuestion = solvedQuestionEvent(solution);
	        int karmaForSolutionAuthor = calculator.karmaFor(solvedQuestion);
	        
	        ReputationEvent markedSolution = markedSolutionEvent(solution);
	        int karmaForQuestionAuthorSolution = calculator.karmaFor(markedSolution);
	        
	        reputationEvents.save(solvedQuestion);
	        reputationEvents.save(markedSolution);
	        solution.getAuthor().increaseKarma(karmaForSolutionAuthor);
	        solution.getMainThread().getAuthor().increaseKarma(karmaForQuestionAuthorSolution);
    	}
    }

    private ReputationEvent solvedQuestionEvent(Answer solution) {
    	return new ReputationEvent(EventType.SOLVED_QUESTION, 
    			solution.getMainThread(), solution.getAuthor());
    }
    
    private ReputationEvent markedSolutionEvent(Answer solution) {
    	return new ReputationEvent(EventType.MARKED_SOLUTION, 
    			solution.getMainThread(), solution.getMainThread().getAuthor());
    }

}
