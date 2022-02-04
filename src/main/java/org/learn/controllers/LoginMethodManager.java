package org.learn.controllers;

import static java.util.Arrays.asList;
import static org.learn.model.SanitizedText.fromTrustedText;

import java.util.List;

import javax.inject.Inject;

import org.learn.auth.Access;
import org.learn.auth.MergeLoginMethod;
import org.learn.auth.SignupInfo;
import org.learn.auth.SocialAPI;
import org.learn.dao.LoginMethodDAO;
import org.learn.dao.UserDAO;
import org.learn.factory.MessageFactory;
import org.learn.model.LoginMethod;
import org.learn.model.MethodType;
import org.learn.model.SanitizedText;
import org.learn.model.User;

import com.google.common.base.Optional;

import br.com.caelum.vraptor.validator.I18nMessage;

public class LoginMethodManager {
	@Inject private UserDAO users;
	@Inject private MergeLoginMethod mergeLoginMethod;
	@Inject private MessageFactory messageFactory;
	@Inject private LoginMethodDAO loginMethods;
	@Inject private Access access;
	
	public boolean merge(MethodType type, SocialAPI socialApi) {
		Optional<SignupInfo> optional = socialApi.getSignupInfo();
		
		if(!optional.isPresent()) return false;
		
		SignupInfo signupInfo = optional.get();
	    
		User existantGoogleUser = users.findByEmailAndMethod(signupInfo.getEmail(), type);
	    
		if(existantGoogleUser != null) {
	    	access.login(existantGoogleUser);
	    	return true;
		}
		
		String token = socialApi.getAccessToken().getToken();
		
		User existantUser = users.findByEmail(signupInfo.getEmail());
		if (existantUser != null) {
			mergeLoginMethod.mergeLoginMethods(token, existantUser, type);
			return true;
		}
		
		createNewUser(token, signupInfo, type);
		return true;
	}
	
	private List<I18nMessage> getConfirmationMessages(User existantUser) {
		List<I18nMessage> messages = asList(messageFactory.build("confirmation", "signup.facebook.existant_brutal", existantUser.getEmail()));
		return messages;
	}
	
	private void createNewUser(String rawToken, SignupInfo signupInfo, MethodType type) {
		User user = new User(fromTrustedText(signupInfo.getName()), signupInfo.getEmail());
		LoginMethod googleLogin = new LoginMethod(type, signupInfo.getEmail(), rawToken, user);
		if (signupInfo.containsPhotoUrl()) {
			user.setPhotoUri(signupInfo.getPhotoUri());
		}
		user.add(googleLogin);
		
		users.save(user);
		loginMethods.save(googleLogin);
		access.login(user);
	}
}
