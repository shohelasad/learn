package org.learn.brutauth.auth.rules;

import br.com.caelum.brutauth.auth.rules.SimpleBrutauthRule;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.events.MethodExecuted;

import org.learn.controllers.EnvironmentAccessLevel;
import org.learn.model.LoggedUser;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import static org.learn.auth.rules.ComposedRule.composedRule;
import static org.learn.auth.rules.Rules.hasKarma;
import static org.learn.auth.rules.Rules.isModerator;

import java.lang.reflect.Method;

@RequestScoped
public class EnvironmentKarmaRule implements SimpleBrutauthRule {

	@Inject
	private EnvironmentKarma environmentKarma;
	@Inject
	private ControllerMethod method;
	@Inject
	private LoggedUser user;

	@Override
	public boolean isAllowed(long l) {
		if (!method.containsAnnotation(EnvironmentAccessLevel.class)) {
			throw new IllegalStateException("To use EnvironmentKarmaRule you must annotate the method with EnvironmentAccessLevel");
		}
		if (!user.isLoggedIn()) {
			return false;
		}
		Method rawMethod = method.getMethod();
		EnvironmentAccessLevel annotation = rawMethod.getAnnotation(EnvironmentAccessLevel.class);
		long karma = environmentKarma.get(annotation.value());
		return composedRule(isModerator())
				.or(hasKarma(karma))
				.isAllowed(user.getCurrent(), null);
	}

}
