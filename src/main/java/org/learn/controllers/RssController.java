package org.learn.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.learn.dao.NewsDAO;
import org.learn.dao.QuestionDAO;
import org.learn.dao.TagDAO;
import org.learn.infra.rss.write.RssFeedFactory;
import org.learn.model.Tag;
import org.learn.model.interfaces.RssContent;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.simplemail.template.BundleFormatter;

@Routed
@Controller
public class RssController {
	private static final int MAX_RESULTS = 30;
	@Inject private RssFeedFactory rssFactory;
	@Inject private QuestionDAO questions;
	@Inject private HttpServletResponse response;
	@Inject private Result result;
	@Inject private TagDAO tags;
	@Inject private NewsDAO news;
	@Inject private BundleFormatter bundle;
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void rss() throws IOException {
		List<RssContent> orderedByDate = questions.orderedByCreationDate(MAX_RESULTS);
		String title = bundle.getMessage("questions.rss.title", bundle.getMessage("site.name"));
		String description = bundle.getMessage("questions.rss.description", bundle.getMessage("site.name"));
		buildRss(orderedByDate, title, description);
	}
	
	@Get
	public void rssByTag(String tagName) throws IOException {
		Tag tag = tags.findByName(tagName);
		if (tag == null) {
			result.notFound();
			return;
		}
		
		List<RssContent> orderedByDate = questions.orderedByCreationDate(MAX_RESULTS, tag);
		String title = bundle.getMessage("questions.rss.title", bundle.getMessage("site.name"));
		String description = bundle.getMessage("questions.rss.description", bundle.getMessage("site.name"));
		buildRss(orderedByDate, title, description);
	}

	@Get
	public void newsRss() throws IOException {
		List<RssContent> orderedByDate = news.orderedByCreationDate(MAX_RESULTS);
		String title = bundle.getMessage("news.rss.title", bundle.getMessage("site.name"));
		String description = bundle.getMessage("news.rss.description", bundle.getMessage("site.name"));
		buildRss(orderedByDate, title, description);
	}
	
	private void buildRss(List<RssContent> orderedByDate, String title, String description) throws IOException {
		OutputStream outputStream = response.getOutputStream();
		response.setContentType("text/xml");
		rssFactory.build(orderedByDate, outputStream, title, description);
		result.nothing();
	}
}
