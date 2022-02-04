package org.learn.model.interfaces;

import org.joda.time.DateTime;
import org.learn.model.User;

public interface RssContent {

	User getAuthor();

	String getTitle();

	Long getId();

	DateTime getCreatedAt();

	String getLinkPath();
	
}
