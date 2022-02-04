package org.learn.dao;

import java.util.List;

import javax.inject.Inject;

import org.hibernate.Query;
import org.hibernate.Session;
import org.learn.model.Comment;
import org.learn.model.Post;
import org.learn.model.User;

public class CommentDAO {

	private Session session;

	@Deprecated
	public CommentDAO() {
	}

	@Inject
	public CommentDAO(Session session) {
		this.session = session;
	}

	public Post loadCommentable(Class<?> type, Long id) {
		return (Post) session.load(type, id);
	}

	public void save(Comment comment) {
		session.save(comment);
	}
	
	public Comment getById(Long id) {
		return (Comment) session.load(Comment.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Comment> flagged(Long minFlagCount) {
		String hql = "select comment from Comment comment left join comment.flags flags group by comment having count(flags) >= :min";
		Query query = session.createQuery(hql);
		return query.setParameter("min", minFlagCount).list();
	}

	public void delete(Comment comment) {
		session.delete(comment);
	}

	public void deleteCommentsOf(User user) {
		List<Comment> comments = session.createQuery("from Comment c where c.author=:user")
				.setParameter("user", user)
				.list();
		for (Comment comment : comments) {
			delete(comment);
		}
	}
}
