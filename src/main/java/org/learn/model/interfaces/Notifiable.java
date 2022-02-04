package org.learn.model.interfaces;

import org.joda.time.DateTime;
import org.learn.model.User;

public interface Notifiable {
    public String getTrimmedContent();
    public DateTime getCreatedAt();
    public String getTypeNameKey();
    public String getEmailTemplate();
    public User getAuthor();
}
