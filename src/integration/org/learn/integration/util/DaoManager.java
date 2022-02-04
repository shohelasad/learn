package org.learn.integration.util;

import static org.learn.model.MarkedText.notMarked;

import java.util.Arrays;
import java.util.Random;

import org.hibernate.Query;
import org.hibernate.Session;
import org.learn.builder.QuestionBuilder;
import org.learn.dao.AnswerDAO;
import org.learn.dao.InvisibleForUsersRule;
import org.learn.dao.LoginMethodDAO;
import org.learn.dao.QuestionDAO;
import org.learn.dao.TagDAO;
import org.learn.dao.UserDAO;
import org.learn.model.Answer;
import org.learn.model.AnswerInformation;
import org.learn.model.LoggedUser;
import org.learn.model.LoginMethod;
import org.learn.model.Question;
import org.learn.model.SanitizedText;
import org.learn.model.Tag;
import org.learn.model.User;
import org.learn.util.ScriptSessionCreator;

public class DaoManager {

	private Session session;
	private QuestionDAO questionDao;
	private AnswerDAO answerDao;
	private UserDAO userDao;
	private TagDAO tagDao;
	private Random randomizer;
	private LoginMethodDAO loginMethodDao;

	public DaoManager() {
		super();
		ScriptSessionCreator sessionFactoryCreator = new ScriptSessionCreator();
		InvisibleForUsersRule invisible = new InvisibleForUsersRule(new LoggedUser(null, null));
		this.randomizer = new Random();

		this.session = sessionFactoryCreator.getSession();
		this.questionDao = new QuestionDAO(session, invisible);
		this.answerDao = new AnswerDAO(session, invisible);
		this.userDao = new UserDAO(session);
		this.tagDao = new TagDAO(session);
		this.loginMethodDao = new LoginMethodDAO(session);
	}

	public Question createQuestion(User author) {
		return createQuestion(author, "Default question title from manager",
				"Default question description from manager", tag("java"));
	}

	public Question createQuestion(User author, String title, String description, Tag... tags) {
		this.session.beginTransaction();

		Question question = new QuestionBuilder()
				.withTitle(title)
				.withDescription(description)
				.withTags(Arrays.asList(tags))
				.withAuthor(author)
				.build();

		this.questionDao.save(question);
		this.session.getTransaction().commit();

		return question;
	}

	public Answer answerQuestion(User author, Question question) {
		return answerQuestion(author, question,
				"Default answer descrition from manager", false);
	}

	public Answer answerQuestion(User author, Question question,
			String description, boolean watching) {
		this.session.beginTransaction();

		LoggedUser loggedUser = new LoggedUser(author, null);
		AnswerInformation information = new AnswerInformation(notMarked(description),
				loggedUser, "new answer");
		Answer answer = new Answer(information, question, author);

		this.answerDao.save(answer);
		this.session.getTransaction().commit();

		return answer;
	}

	public User randomUser() {
		this.session.beginTransaction();

		String email = String.format("acceptance%d@brutal.com", randomizer.nextLong());
		User user = new User(SanitizedText.fromTrustedText("Acceptance Test User"), email);
		LoginMethod brutalLogin = LoginMethod.brutalLogin(user, email, "123456");
		user.add(brutalLogin);

		this.userDao.save(user);
		this.loginMethodDao.save(brutalLogin);
		this.session.getTransaction().commit();

		return user;
	}

	public Tag tag(String name) {
		return tagDao.findByName(name);
	}

	public User moderator() {
		return userDao.findByMailAndPassword("moderator@email.com.br",
				"123456");
	}

	public User karmaNigga() {
		return userDao.findByMailAndPassword("karma.nigga@email.com.br",
				"123456");
	}

	public User user(String email) {
		return userDao.findByMailAndPassword(email, "123456");
	}

	public String getURLFromPasswordToken(String validEmail) {
    	session.beginTransaction();
    	Query query = session.createQuery("select u.id, u.forgotPasswordToken from User u where u.email=:email");
        Object[] result = (Object[]) query.setParameter("email", validEmail).uniqueResult();
        String recoverUrl = "/mudar-senha/"+result[0]+"/"+result[1];
        session.getTransaction().commit();
        return recoverUrl;
	}

}
