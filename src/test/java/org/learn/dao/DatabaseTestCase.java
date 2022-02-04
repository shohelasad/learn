package org.learn.dao;

import static br.com.caelum.vraptor.environment.EnvironmentType.TEST;

import java.io.IOException;

import javax.validation.ValidatorFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.learn.model.User;
import org.learn.model.interfaces.Identifiable;
import org.learn.providers.LearnDatabaseConfiguration;
import org.learn.providers.SessionFactoryCreator;
import org.learn.testcase.CDITestCase;

import br.com.caelum.vraptor.environment.DefaultEnvironment;
import br.com.caelum.vraptor.environment.Environment;

@SuppressWarnings("unchecked")
public abstract class DatabaseTestCase extends CDITestCase{
	protected static final SessionFactory factory;
	private static final SessionFactoryCreator creator;
	protected Session session;
	protected User loggedUser;

	static {
		try {
			ValidatorFactory vf = cdiBasedContainer.instanceFor(ValidatorFactory.class);
			Environment testing = new DefaultEnvironment(TEST);
			LearnDatabaseConfiguration configuration = new LearnDatabaseConfiguration(testing, vf, null);
			configuration.init();
			creator = new SessionFactoryCreator(configuration);
			creator.init();
			factory = creator.getInstance();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected <T> T save(T obj) {
		session.save(obj);
		return obj;
	}

	@Before
	public void beforeDatabase() {
		session = factory.openSession();
		session.beginTransaction();
	}

	@After
	public void afterDatabase() {
		boolean wasActive = session.getTransaction().isActive();
		if (wasActive) {
			session.getTransaction().rollback();
		}
		session.close();
	}
	
	public <T extends Identifiable> T reload(T obj) {
		session.evict(obj);
		return (T) session.load(obj.getClass(), obj.getId());
	}
}
