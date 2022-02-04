package org.learn.dao;


import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.learn.dto.FlaggableAndFlagCount;
import org.learn.meta.LearnMetaInformation;
import org.learn.model.Answer;
import org.learn.model.Comment;
import org.learn.model.Question;
import org.learn.model.User;
import org.learn.model.interfaces.Flaggable;

public class FlaggableDAO {
	private static final long MIN_FLAGS = 2l;

	private final Session session;
	private final LearnMetaInformation meta;

	@Deprecated
	public FlaggableDAO() {
		this(null, null);
	}
	
	@Inject
	public FlaggableDAO(Session session, LearnMetaInformation meta) {
		this.session = session;
		this.meta = meta;
	}
	

	public void turnAllInvisibleWith(User user) {
		List<Class<? extends Flaggable>> types = meta.getFlaggableTypes();
		for (Class<? extends Flaggable> clazz : types) {
			session.createQuery("update "+clazz.getSimpleName()+" set invisible = true where author = :user")
			.setParameter("user", user)
			.executeUpdate();
		}
	}
	
	public Flaggable getById(Long flaggableId, Class<?> clazz) {
		Flaggable flaggable = (Flaggable) session.createQuery("from "+clazz.getSimpleName()+" where id = :id")
				.setLong("id", flaggableId)
				.uniqueResult();
		return flaggable;
	}
	
	@SuppressWarnings("unchecked")
	public List<FlaggableAndFlagCount> flaggedButVisible(Class<?> model) {
		String dtoName = FlaggableAndFlagCount.class.getName();
		String hql = "select new " + dtoName + "(flaggable, count(flags)) from " + model.getSimpleName() + " flaggable " +
				"left join flaggable.flags flags " +
				"where flaggable.moderationOptions.invisible = false " +
				"group by flaggable " +
				"having count(flags) >= :min " +
				"order by count(flags) desc";
		
		Query query = session.createQuery(hql);
		return query.setParameter("min", MIN_FLAGS).list();
	}
	
	public int flaggedButVisibleCount() {
		int comments = flaggedButVisibleCount(Comment.class);
		int questions = flaggedButVisibleCount(Question.class);
		int answers = flaggedButVisibleCount(Answer.class);
		return comments + questions + answers;
	}

	Integer flaggedButVisibleCount(Class<? extends Flaggable> model) {
		String hql = "select flaggable.id from " + model.getSimpleName() + " flaggable " +
				"join flaggable.flags flags " +
				"where flaggable.moderationOptions.invisible = false " +
				"group by flaggable " +
				"having count(flags) >= :min";
		Query query = session.createQuery(hql);
		return (Integer) query.setParameter("min", MIN_FLAGS).list().size();
	}


}
