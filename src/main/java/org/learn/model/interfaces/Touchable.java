package org.learn.model.interfaces;

import org.joda.time.DateTime;
import org.learn.model.User;

public interface Touchable {
	DateTime getLastUpdatedAt();
	User getLastTouchedBy();
	DateTime getCreatedAt();
	User getAuthor();
	boolean isEdited();
}
