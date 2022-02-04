package org.learn.model.interfaces;

import java.io.Serializable;

import org.learn.model.Flag;
import org.learn.model.User;

public interface Flaggable {

	public void add(Flag flag);
	public boolean alreadyFlaggedBy(User user);
	public void remove();
	public boolean isVisible();
	public boolean isVisibleForModeratorAndNotAuthor(User user);
	public Serializable getId();
	
}
