package org.learn.controllers;

import javax.inject.Inject;

import org.learn.brutauth.auth.rules.LoggedRule;
import org.learn.dao.WatcherDAO;
import org.learn.infra.ModelUrlMapping;
import org.learn.model.LoggedUser;
import org.learn.model.User;
import org.learn.model.interfaces.Watchable;
import org.learn.model.watch.Watcher;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class WatchController {

	@Inject private WatcherDAO watchers;
	@Inject private ModelUrlMapping mapping;
	@Inject private LoggedUser currentUser;
	@Inject private Result result;

	@Post
	@CustomBrutauthRules(LoggedRule.class)
	public void watch(Long watchableId, String type) {
		Watchable watchable = watchers.findWatchable(watchableId, mapping.getClassFor(type));
		User user = currentUser.getCurrent();
		Watcher watcher = new Watcher(user);
		watchers.addOrRemove(watchable, watcher);
		result.nothing();
	}
}
