package org.learn.auth;

import javax.inject.Inject;

import org.learn.dao.LoginMethodDAO;
import org.learn.model.LoginMethod;
import org.learn.model.MethodType;
import org.learn.model.User;

public class MergeLoginMethod {
	@Inject private LoginMethodDAO loginMethods;
	@Inject private Access access;
	
	public void mergeLoginMethods(String rawToken, User existantUser, MethodType type) {
		LoginMethod login = LoginMethod.newLogin(existantUser, existantUser.getEmail(), rawToken, type);
		
		existantUser.add(login);
		loginMethods.save(login);
		access.login(existantUser);
	}
}
