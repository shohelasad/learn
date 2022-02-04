package org.learn.model.interfaces;

import java.util.List;

import org.learn.model.watch.Watcher;

public interface Watchable {
	void add(Watcher watcher);
	void remove(Watcher watcher);
	List<Watcher> getWatchers();
	Class<?> getType();
	String getTitle();
}
