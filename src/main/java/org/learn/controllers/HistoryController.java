package org.learn.controllers;

import java.util.List;

import javax.inject.Inject;

import org.learn.auth.rules.PermissionRules;
import org.learn.brutauth.auth.rules.EnvironmentKarma;
import org.learn.brutauth.auth.rules.EnvironmentKarmaRule;
import org.learn.dao.InformationDAO;
import org.learn.dao.ModeratableDao;
import org.learn.dao.ReputationEventDAO;
import org.learn.infra.ModelUrlMapping;
import org.learn.model.Answer;
import org.learn.model.AnswerInformation;
import org.learn.model.EventType;
import org.learn.model.Information;
import org.learn.model.LoggedUser;
import org.learn.model.ModeratableAndPendingHistory;
import org.learn.model.Question;
import org.learn.model.QuestionInformation;
import org.learn.model.ReputationEvent;
import org.learn.model.UpdateStatus;
import org.learn.model.User;
import org.learn.model.interfaces.Moderatable;
import org.learn.reputation.rules.KarmaCalculator;

import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.view.Results;

@Routed
@Controller
public class HistoryController extends BaseController {

	@Inject private Result result;
    @Inject private LoggedUser currentUser;
    @Inject private InformationDAO informations;
	@Inject private ModeratableDao moderatables;
    @Inject private KarmaCalculator calculator;
	@Inject private ModelUrlMapping urlMapping;
	@Inject private ReputationEventDAO reputationEvents;
	@Inject private EnvironmentKarma environmentKarma;

	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	@EnvironmentAccessLevel(PermissionRules.MODERATE_EDITS)
	@Get
	@Path(priority=Path.HIGH, value="")
	public void history() {
		ModeratableAndPendingHistory pendingQuestions = informations.pendingByUpdatables(Question.class);			
		result.include("questions", pendingQuestions);

		ModeratableAndPendingHistory pendingAnswers = informations.pendingByUpdatables(Answer.class);
		result.include("answers", pendingAnswers);
	}

	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	@EnvironmentAccessLevel(PermissionRules.MODERATE_EDITS)
	@Get
	public void unmoderated(String moderatableType) {
		result.redirectTo(this).history();
	}
	
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	@EnvironmentAccessLevel(PermissionRules.MODERATE_EDITS)
	@Get
	public void similarAnswers(Long moderatableId) {
		similar(i18n("answer", "answer.type_name").getMessage(), moderatableId);
	}
	
	
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	@EnvironmentAccessLevel(PermissionRules.MODERATE_EDITS)
	@Get
	public void similarQuestions(Long moderatableId) {
		similar(i18n("question", "question.type_name").getMessage(), moderatableId);
	}
	
	@Get
	public void questionHistory(Long questionId) {
		result.include("histories", informations.historyForQuestion(questionId));
		result.include("post", moderatables.getById(questionId, Question.class));
		result.include("userMediumPhoto", true);
		result.include("isHistoryQuestion", true);
	}

	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	@EnvironmentAccessLevel(PermissionRules.MODERATE_EDITS)
    @Post
    public void publish(Long moderatableId, String moderatableType, Long aprovedInformationId,  String aprovedInformationType) {
    	Class<?> moderatableClass = urlMapping.getClassFor(moderatableType);
    	Information approved = informations.getById(aprovedInformationId, aprovedInformationType);
    	
        Moderatable moderatable = moderatables.getById(moderatableId, moderatableClass);
        List<Information> pending = informations.pendingFor(moderatableId, moderatableClass);
        
        if (!approved.isPending()) {
        	result.use(Results.http()).sendError(403);
        	return;
        }
        
        User approvedAuthor = approved.getAuthor();
        refusePending(aprovedInformationId, pending);
        currentUser.getCurrent().approve(moderatable, approved, environmentKarma);
        ReputationEvent editAppoved = new ReputationEvent(EventType.EDIT_APPROVED, moderatable.getQuestion(), approvedAuthor);
        int karma = calculator.karmaFor(editAppoved);
        approvedAuthor.increaseKarma(karma);
        reputationEvents.save(editAppoved);
        
        result.redirectTo(this).history();
    }
	
	@Post
	public void reject(Long informationId, String typeName) {
		Information informationRefused = informations.getById(informationId, typeName);
		informationRefused.moderate(currentUser.getCurrent(), UpdateStatus.REFUSED);
		Long moderatableId = informationRefused.getModeratable().getId();
		if (typeName.equals(AnswerInformation.class.getSimpleName())) {
			result.redirectTo(this).similarAnswers(moderatableId);
		} else if (typeName.equals(QuestionInformation.class.getSimpleName())) {
			result.redirectTo(this).similarQuestions(moderatableId);
		}
	}

    private void refusePending(Long aprovedHistoryId, List<Information> pending) {
        for (Information refused : pending) {
	        if (!refused.getId().equals(aprovedHistoryId)) {
	            refused.moderate(currentUser.getCurrent(), UpdateStatus.REFUSED);
	        }
        }
    }
    
    private void similar(String moderatableType, Long moderatableId) {
    	Class<?> clazz = urlMapping.getClassFor(moderatableType);
    	result.include("histories", informations.pendingFor(moderatableId, clazz));
    	result.include("post", moderatables.getById(moderatableId, clazz));
    	result.include("type", moderatableType);
    	result.include("userMediumPhoto", true);
    	result.include("isHistoryQuestion", false);
    }
}
