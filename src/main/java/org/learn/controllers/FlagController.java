package org.learn.controllers;

import static br.com.caelum.vraptor.view.Results.http;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import org.learn.auth.rules.PermissionRules;
import org.learn.brutauth.auth.rules.EnvironmentKarmaRule;
import org.learn.brutauth.auth.rules.ModeratorOnlyRule;
import org.learn.dao.AnswerDAO;
import org.learn.dao.FlagDao;
import org.learn.dao.FlaggableDAO;
import org.learn.dao.QuestionDAO;
import org.learn.dto.FlaggableAndFlagCount;
import org.learn.infra.ModelUrlMapping;
import org.learn.model.Answer;
import org.learn.model.Comment;
import org.learn.model.Flag;
import org.learn.model.FlagType;
import org.learn.model.LoggedUser;
import org.learn.model.Question;
import org.learn.model.flag.FlagTrigger;
import org.learn.model.interfaces.Flaggable;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class FlagController {
	
	@Inject private Result result;
	@Inject private FlagDao flags;
	@Inject private LoggedUser loggedUser;
	@Inject private FlaggableDAO flaggables;
	@Inject private QuestionDAO questions;
	@Inject private AnswerDAO answers;
	@Inject private ModelUrlMapping urlMapping;
	@Inject private FlagTrigger flagTrigger;

	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	@EnvironmentAccessLevel(PermissionRules.CREATE_FLAG)
	@Post
	public void addFlag(String flaggableType, Long flaggableId, FlagType flagType, String reason) {
		Class<?> clazz = urlMapping.getClassFor(flaggableType);
		if (flagType == null) {
			result.use(http()).sendError(400);
			return;
		}
		
		if (flags.alreadyFlagged(loggedUser.getCurrent(), flaggableId, clazz)) {
			result.use(http()).sendError(409); //conflict
			return;
		}
		
		Flaggable flaggable = flaggables.getById(flaggableId, clazz);
		flagTrigger.fire(flaggable);

		Flag flag = new Flag(flagType, loggedUser.getCurrent());
		if (flagType.equals(FlagType.OTHER)) {
			flag.setReason(reason);
		}
		
		flags.save(flag);
		flaggable.add(flag);
		
		result.nothing();
	}
	
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	@Get
	@Path(priority=Path.HIGH, value="")
	public void topFlagged() {
		List<FlaggableAndFlagCount> flaggedQuestions = flaggables.flaggedButVisible(Question.class);
		List<FlaggableAndFlagCount> flaggedAnswers = flaggables.flaggedButVisible(Answer.class);
		List<FlaggableAndFlagCount> flaggedComments = flaggables.flaggedButVisible(Comment.class);
		
		List<Question> commentQuestions = new ArrayList<>();

		Iterator<FlaggableAndFlagCount> iterator = flaggedComments.iterator();
		while (iterator.hasNext()) {
			Comment comment = (Comment) iterator.next().getFlaggable();
			Question q = questions.fromCommentId(comment.getId());
			if (q != null) {
				commentQuestions.add(q);
				continue;
			}
			Answer answerFromComment = answers.fromCommentId(comment.getId());
			if (answerFromComment != null) {
				commentQuestions.add(answerFromComment.getQuestion());
				continue;
			}
			// some flags may be related to news (not questions nor answers)
			iterator.remove();
		}


		result.include("questions", flaggedQuestions);
		result.include("answers", flaggedAnswers);
		result.include("comments", flaggedComments);
		result.include("commentQuestions", commentQuestions);
	}

}
