package org.learn.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class NavigationController extends BaseController{

	@Get
	@Path(priority=Path.HIGH, value="")
	public void about() {
		System.out.println("routed to about");
	}
}
