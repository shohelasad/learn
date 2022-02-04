package org.learn.auth.rules;

import org.learn.model.User;

public interface PermissionRule<T> {
    public boolean isAllowed(User u, T item);
}
