package org.learn.providers;

import static java.util.Arrays.asList;

import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.learn.ads.BrutalAds;
import org.learn.auth.BannedUserException;
import org.learn.controllers.AuthController;
import org.learn.factory.MessageFactory;
import org.learn.infra.MenuInfo;
import org.learn.infra.NotFoundException;
import org.learn.infra.SideBarInfo;
import org.learn.util.BrutalDateFormat;
import org.ocpsoft.prettytime.PrettyTime;

import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;

@RequestScoped
public class DefaultViewObjects {

	private static final String SLASH_AT_END = "/$";

	@Inject private Environment env;
	@Inject private Result result;
	@Inject private HttpServletRequest req;
	@Inject private Locale locale;
	@Inject private MenuInfo menuInfo;
	@Inject private BrutalDateFormat brutalDateFormat;
	@Inject private MessageFactory messageFactory;
	@Inject private BrutalAds ads;
	@Inject private SideBarInfo sideBarInfo;

	public void include() {
		menuInfo.include();
		sideBarInfo.include();

		result.include("env", env);
		result.include("prettyTimeFormatter", new PrettyTime(locale));
		result.include("literalFormatter", brutalDateFormat.getInstance("date.joda.pattern"));
		result.include("currentUrl", getCurrentUrl());
		result.include("contextPath", req.getContextPath());
		result.include("deployTimestamp", deployTimestamp());
		result.include("shouldShowAds", ads.shouldShowAds());
		result.on(NotFoundException.class).notFound();
		result.on(BannedUserException.class)
				.include("errors", asList(messageFactory.build("error", "user.errors.banned")))
				.redirectTo(AuthController.class).loginForm("");

	}

	private String deployTimestamp() {
		return System.getProperty("deploy.timestamp", "");
	}

	private String getCurrentUrl() {
		String host = env.get("host");
		String url;
		if (host == null) {
			url = req.getRequestURL().toString();
		} else {
			url = host + req.getRequestURI();
		}
		if(url.endsWith("/")) url = url.split(SLASH_AT_END)[0];
		return url;
	}

}
