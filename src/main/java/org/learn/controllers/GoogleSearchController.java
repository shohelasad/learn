package org.learn.controllers;

import javax.inject.Inject;

import org.learn.environment.EnvironmentDependent;
import org.learn.sanitizer.HtmlSanitizer;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Controller
@EnvironmentDependent(supports="feature.google_search")
@Routed
public class GoogleSearchController {

	@Inject
	private Result result;
	@Inject
	private Environment env;
	@Inject
	private HtmlSanitizer sanitizer;
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void search(String query) {
		result.include("query", sanitizer.sanitize(query));
		result.include("customGoogleSearchKey", env.get("custom_google_search_key"));
	}
}
