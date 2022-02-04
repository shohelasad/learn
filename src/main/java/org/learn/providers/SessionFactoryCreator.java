package org.learn.providers;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.interceptor.Interceptor;

import org.hibernate.SessionFactory;
import org.learn.model.*;
import org.learn.model.watch.Watcher;

@ApplicationScoped
@Alternative
@Priority(Interceptor.Priority.APPLICATION)
public class SessionFactoryCreator {

	public static final String JODA_TIME_TYPE = "org.jadira.usertype.dateandtime.joda.PersistentDateTime";

	private final LearnDatabaseConfiguration cfg;
	private SessionFactory factory;

	/**
	 * @deprecated CDI eyes only
	 */
	public SessionFactoryCreator() {
		this(null);
	}
	
	@Inject
	public SessionFactoryCreator(LearnDatabaseConfiguration cfg) {
		this.cfg = cfg;
	}
	
	@PostConstruct
	public void init() {
		this.factory = cfg.buildSessionFactory();

	}

	@Produces
	@javax.enterprise.context.ApplicationScoped
	public SessionFactory getInstance() {
		return factory;
	}

	void destroy(@Disposes SessionFactory factory) {
		if (!factory.isClosed()) {
			factory.close();
		}
		factory = null;
	}

	public void dropAndCreate() {
		destroy(this.factory);
		cfg.getSchema().drop(true, true);
		cfg.getSchema().create(true, true);
		init();
	}

	public void drop() {
		factory.close();
		factory = null;
		cfg.getSchema().drop(true, true);
		init();
	}


}
