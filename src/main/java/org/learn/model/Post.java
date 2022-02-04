package org.learn.model;

import org.learn.model.interfaces.Commentable;
import org.learn.model.interfaces.Flaggable;
import org.learn.model.interfaces.Touchable;
import org.learn.model.interfaces.Votable;

public interface Post extends Votable, Commentable, Touchable, Flaggable {
	
	String getTypeNameKey();

	void deleteComment(Comment comment);
}