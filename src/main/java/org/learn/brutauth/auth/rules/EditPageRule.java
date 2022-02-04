package org.learn.brutauth.auth.rules;

import static org.learn.auth.rules.ComposedRule.composedRule;
import static org.learn.auth.rules.Rules.hasKarma;
import static org.learn.auth.rules.Rules.isAuthor;
import static org.learn.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import org.learn.auth.rules.PermissionRules;
import org.learn.model.LoggedUser;
import org.learn.model.Page;
import org.learn.model.Question;
import org.learn.model.User;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class EditPageRule implements CustomBrutauthRule{
	private User user;
	private EnvironmentKarma environmentKarma;

	@Deprecated
	public EditPageRule() {
	}

	@Inject
	public EditPageRule(LoggedUser user, EnvironmentKarma environmentKarma) {
		this.environmentKarma = environmentKarma;
		this.user = user.getCurrent();
	}

	public boolean isAllowed(Page page) {
		long karma = environmentKarma.get(PermissionRules.EDIT_PAGE);
		return composedRule(isAuthor()).or(hasKarma(karma)).or(isModerator()).isAllowed(user, page);
	}
}