package org.learn.dao;

import java.util.List;

import org.learn.dao.WithUserPaginatedDAO.OrderType;
import org.learn.model.User;

public interface PaginatableDAO {

	List<?> ofUserPaginatedBy(User author, OrderType order, Integer page);
	
	Long countWithAuthor(User author);

	Long numberOfPagesTo(User author);

}
