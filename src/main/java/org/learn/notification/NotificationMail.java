package org.learn.notification;

import org.learn.mail.action.EmailAction;
import org.learn.model.User;

public class NotificationMail {
	
	private final User watcher;
	private final EmailAction emailAction;

	public NotificationMail(EmailAction emailAction, User watcher) {
		this.emailAction = emailAction;
		this.watcher = watcher;
	}

	public EmailAction getAction() {
		return emailAction;
	}
	
	public User getTo() {
		return watcher;
	}

	public String getEmailTemplate() {
		return emailAction.getWhat().getEmailTemplate();
	}
	
	@Override
	public String toString() {
		return "email to " + watcher;
	}
}
