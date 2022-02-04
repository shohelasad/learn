package org.learn.brutauth.auth.rules;

import javax.inject.Inject;

import org.learn.brutauth.auth.handlers.LoggedHandler;
import org.learn.model.LoggedUser;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;

@HandledBy(LoggedHandler.class)
public class LoggedRule implements CustomBrutauthRule {
	
	@Inject private LoggedUser loggedUser;

	public boolean isAllowed() {
		return loggedUser.isLoggedIn();
	}

}
