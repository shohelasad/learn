package org.learn.controllers;

import java.util.List;

import javax.inject.Inject;

import org.learn.dao.QuestionDAO;
import org.learn.environment.EnvironmentDependent;
import org.learn.model.Question;
import org.learn.model.SanitizedText;
import org.learn.search.QuestionIndex;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Controller
@Routed
@EnvironmentDependent(supports = "feature.solr")
public class SolrSearchController {

	@Inject	private Result result;
	@Inject private QuestionIndex index;
	@Inject private QuestionDAO questions;

	@Get
	@Path(priority=Path.HIGH, value="")
	public void search(SanitizedText query) {
		result.include("query", query.getText());
		result.include("results", doSearch(query, 10));
	}

	@Get
	@Path(priority=Path.HIGH, value="")
	public void questionSuggestion(SanitizedText query, int limit) {
		result.forwardTo(BrutalTemplatesController.class).questionSuggestion(query, doSearch(query, limit));
	}

	private List<Question> doSearch(SanitizedText query, int limit) {
		List<Long> ids = index.find(query.getText(), limit);
		return questions.allVisibleByIds(ids);
	}
}
