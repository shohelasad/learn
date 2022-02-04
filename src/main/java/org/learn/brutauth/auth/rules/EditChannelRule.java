package org.learn.brutauth.auth.rules;

import static org.learn.auth.rules.ComposedRule.composedRule;
import static org.learn.auth.rules.Rules.hasKarma;
import static org.learn.auth.rules.Rules.isAuthor;
import static org.learn.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import org.learn.auth.rules.PermissionRules;
import org.learn.model.Channel;
import org.learn.model.LoggedUser;
import org.learn.model.User;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class EditChannelRule implements CustomBrutauthRule{
	private User user;
	private EnvironmentKarma environmentKarma;

	@Deprecated
	public EditChannelRule() {
	}

	@Inject
	public EditChannelRule(LoggedUser user, EnvironmentKarma environmentKarma) {
		this.environmentKarma = environmentKarma;
		this.user = user.getCurrent();
	}

	public boolean isAllowed(Channel channel) {
		long karma = environmentKarma.get(PermissionRules.EDIT_CHANNEL);
		return composedRule(isAuthor()).or(hasKarma(karma)).or(isModerator()).isAllowed(user, channel);
	}
}