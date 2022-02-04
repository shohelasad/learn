package org.learn.infra;

import static org.learn.model.SanitizedText.fromTrustedText;

import java.util.Random;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.learn.model.LoginMethod;
import org.learn.model.User;

@ApplicationScoped
public class DefaultAdminCreator {
	
	@Inject private SessionFactory sessionFactory;
	
	private static Logger LOG = Logger.getLogger(DefaultAdminCreator.class);
	
	public void createDefaultAdmin() {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Long result = (Long) session.createQuery("select count(*) from User").uniqueResult();
		
		if (result == 0) {
			Random random = new Random(System.currentTimeMillis());
			String email = "moderator@learnqa.org";
			User newUser = new User(fromTrustedText("moderator"), email).asModerator();
			
			String number = Long.toString(random.nextLong());
			String password = Digester.md5(number);
			
			LoginMethod brutalLogin = LoginMethod.brutalLogin(newUser, email, password);
			newUser.add(brutalLogin);
			session.save(brutalLogin);
			session.save(newUser);
			
			LOG.info("=========================");
			LOG.info("New admin user created!");
			LOG.info("email: " + email);
			LOG.info("password: " + password);
			LOG.info("=========================");
		}
		
		session.getTransaction().commit();
	}
}
