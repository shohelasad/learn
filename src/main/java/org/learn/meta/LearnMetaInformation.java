package org.learn.meta;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import org.learn.model.interfaces.Flaggable;

@ApplicationScoped
public class LearnMetaInformation {

	private List<Class<? extends Flaggable>> moderatableTypes = new ArrayList<>();

	public void add(Class<? extends Flaggable> clazz) {
		moderatableTypes.add(clazz);
	}

	public List<Class<? extends Flaggable>> getFlaggableTypes() {
		return moderatableTypes;
	}

}
