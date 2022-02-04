package org.learn.dao;


import static java.util.Collections.EMPTY_LIST;
import static org.hibernate.criterion.Order.desc;
import static org.hibernate.criterion.Projections.rowCount;
import static org.hibernate.criterion.Restrictions.and;
import static org.hibernate.criterion.Restrictions.eq;
import static org.hibernate.criterion.Restrictions.gt;
import static org.hibernate.criterion.Restrictions.in;
import static org.hibernate.criterion.Restrictions.isNull;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.LogManager;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import org.learn.dao.WithUserPaginatedDAO.UserRole;
import org.learn.model.*;
import org.learn.model.interfaces.RssContent;
import org.learn.model.watch.Watcher;
import  org.apache.log4j.Logger;

@SuppressWarnings("unchecked")
public class ChannelDAO {
	
	static final Logger logger = LogManager.getLogger(ChannelDAO.class.getName());
	
    protected static final Integer PAGE_SIZE = 35;
    protected static final Integer CHANNEL_SIZE = 25;
	public static final long SPAM_BOUNDARY = -5;
	
	private Session session;
	private InvisibleForUsersRule invisible;
	private final long pageId = 2;
	
	@Inject 
	private WatcherDAO watcherDao;

	@Deprecated
	public ChannelDAO() {
	}

	@Inject
    public ChannelDAO(Session session, InvisibleForUsersRule invisible) {
        this.session = session;
        this.invisible = invisible;

    }
    
    public void save(Channel c) {
        session.save(c);
    }

	public Channel getById(Long channelId) {
		return (Channel) session.load(Channel.class, channelId);
	}
	
	public Channel load(Channel channel) {
		return getById(channel.getId());
	}
	
	public Channel findByUrl(String url) {
		Channel channel = (Channel) session.createQuery("from Channel c where c.information.url=:url").setString("url", url).uniqueResult();

		return channel;
	}

	public List<Channel> withTagVisible(Tag tag, Integer page, boolean semRespostas) {
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.createAlias("c.information.tags", "t")
				.add(Restrictions.eq("t.id", tag.getId()))
				.addOrder(Order.desc("c.lastUpdatedAt"))
				.setFirstResult(firstResultOf(page))
				.setMaxResults(PAGE_SIZE)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if (semRespostas) {
			criteria.add(Restrictions.eq("c.answerCount", 0l));
		}

		
		return addInvisibleFilter(criteria).list();
	}

	public List<Channel> hot(DateTime since, int count) {
		return session.createCriteria(Channel.class, "c")
				.add(gt("c.createdAt", since))
				.add(and(Restrictions.eq("c.moderationOptions.invisible", false)))
				.addOrder(Order.desc("c.voteCount"))
				.setMaxResults(count)
				.list();
	}
	
	public List<Channel> top(String section, int count, DateTime since) {
		Order order;
		if (section.equals("viewed")) {
			order = Order.desc("c.views");
		}
		else /*if (section.equals("voted"))*/ {
			order = Order.desc("c.voteCount");
		}
		return session.createCriteria(Channel.class, "c")
				.add(and(Restrictions.eq("c.moderationOptions.invisible", false)))
				.add(gt("c.createdAt", since))
				.addOrder(order)
				.setMaxResults(count)
				.list();
	}

	public long numberOfPages() {
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.setProjection(rowCount());
		Long totalItems = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(totalItems);
	}

	public long numberOfPages(Tag tag) {
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.createAlias("c.information", "ci")
				.createAlias("ci.tags", "t")
				.add(eq("t.id", tag.getId()))
				.setProjection(rowCount());
		Long totalItems = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(totalItems);
	}

	public Long totalPagesUnsolvedVisible() {
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.add(isNull("c.solution"))
				.setProjection(rowCount());
		Long result = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(result);
	}
	
	public Long totalPagesWithoutAnswers() {
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.add(Restrictions.eq("c.answerCount", 0l))
				.setProjection(rowCount());
		Long result = (Long) addInvisibleFilter(criteria).list().get(0);
		return calculatePages(result);
	}

	private int firstResultOf(Integer page) {
		return PAGE_SIZE * (page-1);
	}

	private long calculatePages(Long count) {
		long result = count/PAGE_SIZE.longValue();
		if (count % PAGE_SIZE.longValue() != 0) {
			result++;
		}
		return result;
	}

	private Criteria addInvisibleFilter(Criteria criteria) {
		return invisible.addFilter("c", criteria);
	}

	public List<Channel> withTagVisible(Tag tag, int page) {
		return withTagVisible(tag, page, false);
	}

	public void delete(Channel channel) {
		session.delete(channel);
	}

	public List<Channel> findChannels() {
		
		return session.createQuery("from Channel").list();
	}
	
	public List<Channel> findChannels(Integer p, boolean semRespostas) {
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.addOrder(Order.desc("c.lastUpdatedAt"))
				.setFirstResult(firstResultOf(p))
				.setMaxResults(PAGE_SIZE)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		if (semRespostas) {
			criteria.add(Restrictions.eq("c.questionCount", 0l));
		}
		
		return addInvisibleFilter(criteria).list();
	}
	
	public List<Channel> getFollowingChannels(Tag tag, User currentUser) {	
		String hql = "select w from Watcher w where w.watcher.id = :id";
		Query query = session.createQuery(hql);
		List<Watcher> watchers = (List<Watcher>) query.setParameter("id", currentUser.getId()).list();
		//List<Watcher> watchers = (List<Watcher>) session.createQuery("from Watcher w where w.watcher.id = :id").setLong("id", currentUser.getId());
		
		List<Channel> channels = new ArrayList<>();
		watchers.forEach(w-> {
			channels.addAll(w.getChannels());
		});

		return channels;
	}
	
	public List<Channel> getRelatedChannels(Tag tag, User currentUser) {	
		List<Channel> channels = session.createCriteria(Channel.class, "c")
				.add(Restrictions.eq("c.deleted", false))
				//.createAlias("c.information.tags", "tags")
				//.add(Restrictions.eq("tags.id", tag.getId()))
				.addOrder(Order.desc("c.createdAt"))
				.setMaxResults(CHANNEL_SIZE)
				.list();
		
		List<Channel> relatedChannels = new ArrayList<>();
		// i random number of number of channel, j try for not following
		int i = getRandomNumber(channels.size() - 1);
		int j = 0;
		while(i < channels.size()) {
			Channel c = channels.get(i);
			if(!watcherDao.ping(c, currentUser)){
				relatedChannels.add(c);
			}
			
			if(relatedChannels.size() > 2 || j > 5)
				break;
			
			i++; j++;
		}
		
		return relatedChannels;
	}
	
	public List<Channel> getRelatedChannelsByChannel(Channel channel, Tag tag, User currentUser) {
		if (channel.hasTags()) {
			List<Channel> channels = session.createCriteria(Channel.class, "c")
					.add(Restrictions.eq("c.deleted", false))
					.createAlias("c.information.tags", "tags")
					.add(Restrictions.eq("c.parent.id", channel.getId()))
					.add(Restrictions.eq("tags.id", tag.getId()))
					.addOrder(Order.desc("c.createdAt"))
					.setMaxResults(CHANNEL_SIZE)
					.list();
			
			List<Channel> relatedChannels = new ArrayList<>();
			// i random number of number of channel, j try for not following
			int i = getRandomNumber(channels.size() - 1);
			int j = 0;
			while(i < channels.size()) {
				Channel c = channels.get(i);
				if(!watcherDao.ping(c, currentUser)){
					relatedChannels.add(c);
				}
				
				if(relatedChannels.size() > 2 || j > 5)
					break;
				
				i++; j++;
			}
			
			return relatedChannels;
		}
		
		return EMPTY_LIST;
	}
	
	//question is not strict to channel
	public List<Channel> getRelatedChannelsByQuestion(Question question, User currentUser) {
		if (question.hasTags()) {
			List<Channel> channels = session.createCriteria(Channel.class, "c")
					.createAlias("c.information.tags", "tags")
					.add(Restrictions.eq("tags.id", question.getMostImportantTag().getId()))
					.addOrder(Order.desc("c.createdAt"))
					.setMaxResults(CHANNEL_SIZE)
					.list();
			
			List<Channel> relatedChannels = new ArrayList<>();
			// i random number of number of channel, j try for not following
			int i = getRandomNumber(channels.size() - 1);
			int j = 0;
			while(i < channels.size()) {
				Channel c = channels.get(i);
				if(!watcherDao.ping(c, currentUser)){
					relatedChannels.add(c);
				}
				
				if(relatedChannels.size() > 2 || j > 5)
					break;
				
				i++; j++;
			}
			
			return relatedChannels;
		}
		
		return EMPTY_LIST;
	}
	
	/*public List<Channel> findChannelsByChannel(Channel channel, User currentUser) {
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.createAlias("c.information", "i")
				.add(Restrictions.eq("c.parent.id", channel.getId()))
				.addOrder(Order.desc("c.id"))
				.setFirstResult(firstResultOf(1))
				.setMaxResults(CHANNEL_SIZE)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Channel> channels = addInvisibleFilter(criteria).list();
		
		return channels;					
	}*/
	
	/*public List<Channel> findChannelsByFollowing(Channel channel, User currentUser) {
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.createAlias("c.information", "i")
				.add(Restrictions.eq("c.parent.id", channel.getId()))
				.addOrder(Order.desc("c.id"))
				.setFirstResult(firstResultOf(1))
				.setMaxResults(CHANNEL_SIZE)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Channel> channels = addInvisibleFilter(criteria).list();
		
		List<Channel> channelFollowing = new ArrayList<>();
		channels.forEach(c-> {
			c.getWatchers().forEach(watcher-> {
				if(watcher.getWatcher().getId().equals(currentUser.getId())){
					channelFollowing.add(c);
				}
			});
		});
		
		return channelFollowing;					
	}*/
	
	/*public List<Channel> findChannelsByNotFollowing(Channel channel, User currentUser) {	
		Criteria criteria = session.createCriteria(Channel.class, "c")
				.createAlias("c.information", "i")
				.add(Restrictions.eq("c.parent.id", channel.getId()))
				.addOrder(Order.desc("c.id"))
				.setFirstResult(firstResultOf(1))
				.setMaxResults(CHANNEL_SIZE)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		
		List<Channel> channels = addInvisibleFilter(criteria).list();
		
		List<Channel> channelFollowing = new ArrayList<>();
		channels.forEach(c-> {
			c.getWatchers().forEach(watcher-> {
				if(watcher.getWatcher().getId().equals(currentUser.getId())){
					channelFollowing.add(c);
				}
			});
		});
		
		channels.removeAll(channelFollowing);
		
		return channels;			
	}*/
	
	
	private int getRandomNumber(int n){
		return (int) Math.random();
	}
	
}
