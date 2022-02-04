package org.learn.controllers;

import javax.inject.Inject;

import org.learn.auth.GoogleAPI;
import org.learn.auth.SocialAPI;
import org.learn.model.MethodType;
import org.learn.qualifiers.Google;
import org.learn.validators.UrlValidator;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import br.com.caelum.brutauth.auth.annotations.Public;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;

@Public
@Controller
public class GoogleAuthController extends BaseController{
	
	@Inject @Google private OAuthService service;
	@Inject private UrlValidator urlValidator;
	@Inject private LoginMethodManager loginManager;

	@Get("/sign-up/google/")
	public void signUpViaGoogle(String state, String code) {
		Token token = service.getAccessToken(null, new Verifier(code));
		SocialAPI googleAPI = new GoogleAPI(token, service);
	    
		loginManager.merge(MethodType.GOOGLE, googleAPI);
		
	    redirectToRightUrl(state);
	}
	
	private void redirectToRightUrl(String state) {
		boolean valid = urlValidator.isValid(state);
		if (!valid) {
			includeAsList("learnMessages", i18n("error", "error.invalid.url", state));
		}
        if (state != null && !state.isEmpty() && valid) {
            redirectTo(state);
        } else {
            redirectTo(ListController.class).home(null);
        }
	}
}
