package org.learn.model.flag;

import org.learn.model.interfaces.Flaggable;

public interface FlagAction {

	void fire(Flaggable flaggable);

	boolean shouldHandle(Flaggable flaggable);

}
