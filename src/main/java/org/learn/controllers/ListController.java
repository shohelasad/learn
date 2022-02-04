package org.learn.controllers;

import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Get;
import br.com.caelum.vraptor.Path;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.actioncache.Cached;
import br.com.caelum.vraptor.routes.annotation.Routed;
import org.joda.time.DateTime;
import org.learn.components.RecentTagsContainer;
import org.learn.dao.NewsDAO;
import org.learn.dao.ChannelDAO;
import org.learn.dao.QuestionDAO;
import org.learn.dao.TagDAO;
import org.learn.dao.WatcherDAO;
import org.learn.factory.MessageFactory;
import org.learn.model.Channel;
import org.learn.model.LoggedUser;
import org.learn.model.News;
import org.learn.model.Channel;
import org.learn.model.Question;
import org.learn.model.Tag;
import org.learn.model.User;
import org.learn.stream.Streamed;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static java.util.Arrays.asList;

import java.util.ArrayList;

@Routed
@Controller
public class ListController {

	protected static final Integer FEATURED_SIZE = 50; // default FEATURED_SIZE = 10 
	protected static final Integer SINCE_MONTH = 6;
	
	@Inject private LoggedUser loggedUser;
	@Inject private QuestionDAO questionDao;
	@Inject private Result result;
	@Inject private ChannelDAO channelDao;
	@Inject private TagDAO tagDao;
	@Inject private RecentTagsContainer recentTagsContainer;
	@Inject private NewsDAO newses;
	@Inject private RecentTagsContainer tagsContainer;
	@Inject private HttpServletResponse response;
	@Inject private MessageFactory messageFactory;
	@Inject private WatcherDAO watchers;
	
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void home(Integer p) {
		Integer page = getPage(p);
		List<Question> visible = questionDao.answered(page);
		if (visible.isEmpty() && page != 1) {
			result.notFound();
			return;
		}
		
		result.include("questions", visible);
		
		if(page < 2) {
			List<String> tabs = asList("voted", "answered", "viewed");
			result.include("tabs", tabs);
			//result.include("questions", questionDao.answered(page));
			
			result.include("totalPages", questionDao.numberOfPages());
			result.include("currentPage", page);
			result.include("currentUser", loggedUser);
	
			if(loggedUser.getCurrent() != User.GHOST) {
				//find tag by user activity and following
				Tag tag = null;
				result.include("relatedChannels", channelDao.getRelatedChannels(tag, loggedUser.getCurrent()));
				result.include("followingChannels", channelDao.getFollowingChannels(tag, loggedUser.getCurrent()));
			}
			
			result.include("featuredQuestions", questionDao.getRecentQuestions("/"));	
			result.include("isHomeMenu", true);
			result.include("isTagVisible", true);

		}
	}
	
	/*@Get
	@Path(priority=Path.HIGH, value="")
	public void content(Integer p) {
		Integer page = getPage(p);
		List<Question> visible = questionDao.answered(page);
		if (visible.isEmpty() && page != 1) {
			result.notFound();
			return;
		}
		
		result.include("questions", visible);
	}*/
	

	@Streamed
	@Path(priority=Path.HIGH, value="")
	public void streamedHome(Integer p) {
		List<String> tabs = asList("voted", "answered", "viewed");
		result.include("tabs", tabs);
		result.include("currentUser", loggedUser);
	}

	@Cached(key="questionListPagelet", duration = 30, idleTime = 30)
	@Streamed
	@Path(priority=Path.HIGH, value="")
	public void questionListPagelet(Integer p) {
		List<String> tabs = asList("voted", "answered", "viewed");
		result.include("tabs", tabs);
		result.include("currentUser", loggedUser);
		Integer page = getPage(p);
		List<Question> visible = questionDao.allVisible(page);
		if (visible.isEmpty() && page != 1) {
			result.notFound();
			return;
		}

		result.include("questions", visible);
		result.include("totalPages", questionDao.numberOfPages());
		result.include("currentPage", page);
		result.forwardTo("/WEB-INF/jsp/list/questionListPagelet.jspf");
	}

	@Cached(key="sideBarPagelet", duration = 30, idleTime = 30)
	@Streamed
	@Path(priority=Path.HIGH, value="")
	public void sideBarPagelet() {
		result.include("sidebarNews", newses.allVisibleAndApproved(5));
		result.include("recentTags", tagsContainer.getRecentTagsUsage());
		result.forwardTo("/WEB-INF/jsp/list/sideBarPagelet.jspf");

	}

	@Get
	@Path(priority=Path.HIGH, value="")
	public void topRaw() {
		result.redirectTo(this).top("voted");
	}

	@Get
	public void top(String section) {
		Integer count = 35;
		
		List<String> tabs = asList("voted", "answered", "viewed");
		if (!tabs.contains(section)) {
			section = tabs.get(0);
			result.redirectTo(this).top(section);
			return;
		}

		DateTime since = DateTime.now().minusMonths(2);
		List<Question> top = questionDao.top(section, count, since);
		
		if (top.isEmpty()) {
			result.notFound();
			return;
		}
		result.include("tabs", tabs);
		result.include("section", section);
		result.include("questions", top);
		result.include("currentUser", loggedUser);
	}
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void hackedIndex() {
		result.redirectTo(this).home(1);
	}

	@Get
	@Path(priority=Path.HIGH, value="")
	public void news(Integer p) {
		Integer page = getPage(p);
		List<News> visible = newses.allVisible(page, 25);
		if (visible.isEmpty() && page != 1) {
			result.notFound();
			return;
		}
		result.include("newses", visible);
		result.include("totalPages", newses.numberOfPages(25));
		result.include("currentPage", page);
		result.include("newsActive", true);
		result.include("noDefaultActive", true);
	}
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void unsolved(Integer p) {
		Integer page = getPage(p);
		result.include("questions", questionDao.unsolvedVisible(page));
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("currentPage", page);
		result.include("totalPages", questionDao.totalPagesUnsolvedVisible());
	}
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void unanswered(Integer p) {
		Integer page = getPage(p);
		result.include("questions", questionDao.unanswered(page));
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("currentPage", page);
		result.include("totalPages", questionDao.totalPagesWithoutAnswers());
		result.include("unansweredActive", true);
		result.include("noDefaultActive", true);
		result.include("isHomePage", true);
		result.include("isTagVisible", true);
	}
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void answered(Integer p) {
		Integer page = getPage(p);
		result.include("questions", questionDao.answered(page));
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("currentPage", page);
		result.include("totalPages", questionDao.totalPagesWithoutAnswers());
		result.include("answeredActive", true);
		result.include("noDefaultActive", true);
	}
	
	
	@Get
	//@Path(priority=Path.LOW, value="")
	public void withChannel(String pageName, Integer p, boolean semRespostas){	
		Channel channel = channelDao.findByUrl(pageName);
		result.include("channel", channel);
		Integer pageNo = getPage(p);
		//TODO: find question page tags
		Tag tag = tagDao.findByName(pageName);
		if(tag == null){
			result.notFound();
			return;
		}
		List<Question> questionsWithTag = questionDao.withTagVisible(tag, pageNo, semRespostas);
		result.include("totalPages", questionDao.numberOfPages(tag));
		result.include("tag", tag);
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("questions", questionsWithTag);
		result.include("currentPage", pageNo);
		result.include("hasAbout", tagDao.hasAbout(tag));
		
		if(loggedUser.getCurrent() != User.GHOST && channel != null) {
			List<Channel> rchannels = channelDao.getRelatedChannelsByChannel(channel, tag, loggedUser.getCurrent());
			//result.include("relatedChannels", channelDao.getRelatedChannelsByChannel(channel, tag, loggedUser.getCurrent()));
			
			result.include("followingChannels", rchannels);
		}
		
		result.include("featuredQuestions", questionDao.getRecentQuestions(pageName));	
		//result.include("recentQuestions", questionDao.getRecentQuestions(channel.getTitle()));	
		//List<Channel> channelsWithTag = channelDao.withTagVisible(tag, pageNo, semRespostas);
		//result.include("relatedPage", channelsWithTag);
		
		if (semRespostas) {
			result.include("unansweredActive", true);
			result.include("noDefaultActive", true);
			result.include("unansweredTagLinks", true);
		}
		
		User current = loggedUser.getCurrent();
		//boolean isWatching = watchers.ping(channel, current);
		boolean isFollowing = watchers.ping(channel, current);
		result.include("isFollowing", isFollowing);
		result.include("isTagVisible", true);
		result.include("noDefaultActive", false);
		result.include(pageName + "Active", true);
	}
	
	@Get
	public void withTag(String tagName, Integer p, boolean semRespostas) {
		Integer page = getPage(p);
		Tag tag = tagDao.findByName(tagName);
		if(tag == null){
			result.notFound();
			return;
		}
		List<Question> questionsWithTag = questionDao.withTagVisible(tag, page, semRespostas);
		result.include("totalPages", questionDao.numberOfPages(tag));
		result.include("tag", tag);
		result.include("recentTags", recentTagsContainer.getRecentTagsUsage());
		result.include("questions", questionsWithTag);
		result.include("currentPage", page);
		result.include("hasAbout", tagDao.hasAbout(tag));
		if (semRespostas) {
			result.include("unansweredActive", true);
			result.include("noDefaultActive", true);
			result.include("unansweredTagLinks", true);
		}
		
	}
	
	@Get
	@Path(priority=Path.HIGH, value="")
	public void listTags(){
		result.include("tags", tagDao.all());
	}
	
	private Integer getPage(Integer p) {
		Integer page = p == null ? 1 : p;
		return page;
	}

}
