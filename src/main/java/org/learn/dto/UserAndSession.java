package org.learn.dto;

import org.learn.model.User;
import org.learn.model.UserSession;

public class UserAndSession {

	private final User user;
	private final UserSession userSession;

	public UserAndSession(User user, UserSession userSession) {
		this.user = user;
		this.userSession = userSession;
	}

	public User getUser() {
		return user;
	}

	public UserSession getUserSession() {
		return userSession;
	}

}
