package org.learn.auth;

import org.learn.dao.UserDAO;
import org.learn.model.User;

import javax.inject.Inject;

public class LDAPAuthenticator implements Authenticator {
	@Inject private UserDAO users;
	@Inject private Access system;
	@Inject private LDAPApi ldap;

	public boolean authenticate(String username, String password) {
		//auth credentials
		if (!ldap.authenticate(username, password)) {
			return false;
		}

		String email = ldap.getEmail(username);
		User retrieved = users.findByEmail(email);
		if (retrieved == null) {
			retrieved = users.findByMailAndLegacyPasswordAndUpdatePassword(email, password);
		}
		if (retrieved == null) {
			return false;
		}

		system.login(retrieved);
		return true;
	}

	public void signout() {
		system.logout();
	}
}
