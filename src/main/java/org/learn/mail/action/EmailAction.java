package org.learn.mail.action;

import org.learn.model.Post;
import org.learn.model.interfaces.Notifiable;
import org.learn.model.interfaces.Watchable;

public class EmailAction {
	private final Notifiable notifiable;
	private final Post post;

	public EmailAction(Notifiable notifiable, Post post) {
		this.notifiable = notifiable;
		this.post = post;
	}

	public Notifiable getWhat() {
		return notifiable;
	}

	public Post getWhere() {
		return post;
	}

	public Watchable getMainThread() {
		return post.getMainThread();
	}

}
