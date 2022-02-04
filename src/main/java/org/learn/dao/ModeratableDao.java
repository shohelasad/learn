package org.learn.dao;

import javax.inject.Inject;

import org.hibernate.Session;
import org.learn.model.interfaces.Moderatable;

public class ModeratableDao {
	private Session session;

	@Deprecated
	public ModeratableDao() {
	}

	@Inject
    public ModeratableDao(Session session) {
		this.session = session;
	}

	public Moderatable getById(Long id, Class<?> clazz) {
        return (Moderatable) session.load(clazz, id);
    }

}
