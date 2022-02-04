package org.learn.observer;

import org.learn.dao.UserDAO;
import org.learn.dao.WatcherDAO;
import org.learn.event.QuestionCreated;
import org.learn.mail.NewQuestionMailer;
import org.learn.mail.action.EmailAction;
import org.learn.model.Question;
import org.learn.model.User;
import org.learn.model.watch.Watcher;
import org.learn.notification.NotificationMail;
import org.learn.notification.NotificationMailer;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.List;

public class SubscribeUsersToNewQuestion {

	@Inject
	private UserDAO users;
	@Inject
	private WatcherDAO watchers;
	@Inject
	private NewQuestionMailer newQuestionMailer;

	public void subscribeUsers(@Observes QuestionCreated questionCreated) {
		Question question = questionCreated.getQuestion();
		List<User> subscribed = users.findUsersSubscribedToAllQuestions();
		for (User user : subscribed) {
			watchers.add(question, new Watcher(user));
		}
		newQuestionMailer.send(subscribed, question);

	}
}
