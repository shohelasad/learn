package org.learn.brutauth.auth.rules;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.learn.brutauth.auth.handlers.InputHandler;
import org.learn.input.InputManager;
import org.learn.model.LoggedUser;
import org.learn.model.User;

import br.com.caelum.brutauth.auth.annotations.HandledBy;
import br.com.caelum.brutauth.auth.rules.CustomBrutauthRule;
import br.com.caelum.vraptor.environment.Environment;

@HandledBy(InputHandler.class)
public class InputRule implements CustomBrutauthRule{

	@Inject public LoggedUser user;
	@Inject public InputManager input;
	@Inject public HttpServletRequest request;
	@Inject public Environment env;
	
	public boolean isAllowed(){
		if(!env.supports("feature.input.rule")) return true;
		User current = user.getCurrent();
		boolean isAllowed = input.can(current);
		if(isAllowed) input.ping(current);
		return isAllowed;
	}

}
