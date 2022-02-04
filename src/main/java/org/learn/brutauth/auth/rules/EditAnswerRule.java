package org.learn.brutauth.auth.rules;

import static org.learn.auth.rules.ComposedRule.composedRule;
import static org.learn.auth.rules.Rules.hasKarma;
import static org.learn.auth.rules.Rules.isAuthor;
import static org.learn.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import org.learn.auth.rules.PermissionRules;
import org.learn.model.Answer;
import org.learn.model.LoggedUser;
import org.learn.model.User;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class EditAnswerRule implements CustomBrutauthRule {
	private User user;
	private EnvironmentKarma environmentKarma;

	@Deprecated
	public EditAnswerRule() {
	}
	
	@Inject
	public EditAnswerRule(LoggedUser user, EnvironmentKarma environmentKarma) {
		this.environmentKarma = environmentKarma;
		this.user = user.getCurrent();
	}

	public boolean isAllowed(Answer answer) {
		long karma = environmentKarma.get(PermissionRules.EDIT_ANSWER);
		return composedRule(isAuthor()).or(hasKarma(karma)).or(isModerator()).isAllowed(user, answer);
	}
}

