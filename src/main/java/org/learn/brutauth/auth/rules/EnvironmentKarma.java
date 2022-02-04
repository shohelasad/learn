package org.learn.brutauth.auth.rules;

import br.com.caelum.vraptor.environment.Environment;

import javax.inject.Inject;
import javax.inject.Named;

import org.learn.auth.rules.PermissionRules;

@Named("environmentKarma")
public class EnvironmentKarma {

	private Environment environment;

	@Deprecated
	EnvironmentKarma() {
	}

	@Inject
	public EnvironmentKarma(Environment environment) {
		this.environment = environment;
	}

	public long get(PermissionRules rule) {
		String accessLevelString = environment.get("permission.rule." + rule.getPermissionName());
		long karma = Long.parseLong(accessLevelString);
		return karma;
	}
}
