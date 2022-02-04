package org.learn.search;

import static org.learn.model.SanitizedText.fromTrustedText;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.hibernate.Session;
import org.learn.dao.InvisibleForUsersRule;
import org.learn.dao.QuestionDAO;
import org.learn.model.LoggedUser;
import org.learn.model.Question;
import org.learn.model.User;
import org.learn.providers.SessionFactoryCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.environment.Environment;
import br.com.caelum.vraptor.events.VRaptorInitialized;

@ApplicationScoped
public class IndexSyncJob {
	public static final Logger LOGGER = LoggerFactory.getLogger(IndexSyncJob.class);

	private QuestionIndex index;
	private Environment environment;
	private SessionFactoryCreator factory;

	@Deprecated
	protected IndexSyncJob() {
	}

	@Inject
	public IndexSyncJob(QuestionIndex index, Environment environment, SessionFactoryCreator factory) {
		this.index = index;
		this.environment = environment;
		this.factory = factory;
	}

	public void execute() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Session session = factory.getInstance().openSession();
				try {
					QuestionDAO questions = new QuestionDAO(session, generateUser());

					long pages = questions.numberOfPages();
					long total = 0;
					LOGGER.info("Syncing questions!");
					for (int i = 0; i < pages; i++) {
						List<Question> q = questions.allVisible(i);
						index.indexQuestionBatch(q);
						total += q.size();
					}
					LOGGER.info("Synced " + total + " questions");
				} finally {
					session.close();
				}
			}
		}).start();
	}

	private InvisibleForUsersRule generateUser() {
		User user = new User(fromTrustedText("System"), "system");
		LoggedUser loggedUser = new LoggedUser(user, null);
		return new InvisibleForUsersRule(loggedUser);
	}

	public void onStartup(@Observes VRaptorInitialized init) {
		if (environment.supports("solr.syncOnStartup") && environment.supports("feature.solr")) {
			execute();
		}
	}
}