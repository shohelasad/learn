package org.learn.brutauth.auth.rules;

import static org.learn.auth.rules.ComposedRule.composedRule;
import static org.learn.auth.rules.Rules.isAuthor;
import static org.learn.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import org.learn.model.LoggedUser;
import org.learn.model.News;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class EditNewsRule implements CustomBrutauthRule{
	@Inject private LoggedUser user;
	
	public boolean isAllowed(News news) {
		return composedRule(isModerator()).or(isAuthor()).isAllowed(user.getCurrent(), news);
	}
}
