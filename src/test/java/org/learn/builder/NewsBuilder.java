package org.learn.builder;


import org.learn.model.LoggedUser;
import org.learn.model.MarkedText;
import org.learn.model.News;
import org.learn.model.NewsInformation;
import org.learn.model.SanitizedText;
import org.learn.model.User;

public class NewsBuilder extends ModelBuilder {
	private String title = "default news";
	private MarkedText description = MarkedText.notMarked("default news default news default news");
	private String comment = "blablaba";
	private User author = new User(SanitizedText.fromTrustedText("author"), "newsauthor@gmail.com");
	private LoggedUser loggedUser = null;
	private Long id = null;
	
	public News build() {
		if (loggedUser == null) {
			loggedUser = new LoggedUser(author, null);
		}
		NewsInformation newsInformation = new NewsInformation(title, description, loggedUser, comment);
		News news = new News(newsInformation, author);
		setId(news, id);
		return news;
	}

	public NewsBuilder withId(long id) {
		this.id = id;
		return this;
	}
	

}
