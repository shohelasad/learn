package org.learn.infra;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.learn.factory.MessageFactory;
import org.learn.model.Answer;
import org.learn.model.Channel;
import org.learn.model.Comment;
import org.learn.model.News;
import org.learn.model.Question;
import org.learn.model.User;

@ApplicationScoped
public class ModelUrlMapping {

    private Map<String, Class<?>> classForUrl = new HashMap<>();

    @Deprecated
    public ModelUrlMapping() { }

    @Inject
    public ModelUrlMapping(MessageFactory messageFactory) {
		classForUrl.put(messageFactory.build("question", "question.type_name").getMessage(), Question.class);
		classForUrl.put(Question.class.getSimpleName(), Question.class);
		classForUrl.put(messageFactory.build("channel", "channel.type_name").getMessage(), Channel.class);
		classForUrl.put(Channel.class.getSimpleName(), Channel.class);
		classForUrl.put(messageFactory.build("answer", "answer.type_name").getMessage(), Answer.class);
		classForUrl.put(Answer.class.getSimpleName(), Answer.class);
		classForUrl.put(messageFactory.build("comment", "comment.type_name").getMessage(), Comment.class);
		classForUrl.put(Comment.class.getSimpleName(), Comment.class);
		classForUrl.put(messageFactory.build("news", "news.type_name").getMessage(), News.class);
		classForUrl.put(News.class.getSimpleName(), News.class);
		classForUrl.put(messageFactory.build("user", "user.type_name").getMessage(), User.class);
		classForUrl.put(User.class.getSimpleName(), User.class);
	}
	
	public Class<?> getClassFor(String urlParam){
		Class<?> clazz = classForUrl.get(urlParam);
		if (clazz == null) {
			throw new NotFoundException("There is no model mapped to that urlParam: "+ urlParam);
		}
		return clazz;
	}
}
