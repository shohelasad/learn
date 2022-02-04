package org.learn.brutauth.auth.rules;

import static org.learn.auth.rules.Rules.isModerator;

import javax.inject.Inject;

import org.learn.model.LoggedUser;

import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

public class ModeratorOnlyRule implements CustomBrutauthRule{
	
	@Inject private LoggedUser user;

	
	public boolean isAllowed(){
		return isModerator().isAllowed(user.getCurrent(), null);
	}
	
}
