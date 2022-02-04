package org.learn.model.interfaces;

import java.io.Serializable;
import java.util.List;

import org.learn.model.Comment;
import org.learn.model.User;


public interface Commentable {

	Comment add(Comment comment);
	Serializable getId();
	List<Comment> getVisibleCommentsFor(User user);
	Watchable getMainThread();

}
