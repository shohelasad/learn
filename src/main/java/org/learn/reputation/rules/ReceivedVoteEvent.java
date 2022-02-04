package org.learn.reputation.rules;

import java.util.HashMap;
import java.util.Map;

import org.learn.model.Answer;
import org.learn.model.Comment;
import org.learn.model.EventType;
import org.learn.model.News;
import org.learn.model.Question;
import org.learn.model.ReputationEvent;
import org.learn.model.ReputationEventContext;
import org.learn.model.VoteType;
import org.learn.model.interfaces.Votable;

public class ReceivedVoteEvent {
	
	private static final Map<Class<? extends Votable>, VotableRule> map = new HashMap<>();
	private final VotableRule rule;
	private final VoteType type;
	private final Votable votable;
	private final ReputationEventContext eventContext;
	private final boolean shouldCountKarma;
	{
		map.put(Question.class, new QuestionVoteRule());
		map.put(Answer.class, new AnswerVoteRule());
		map.put(Comment.class, new CommentVoteRule());
		map.put(News.class, new NewsVoteRule());
	}
	
	public ReceivedVoteEvent(VoteType type, Votable votable, ReputationEventContext eventContext, boolean shouldCountKarma) {
		this.type = type;
		this.votable = votable;
		this.eventContext = eventContext;
		this.shouldCountKarma = shouldCountKarma;
		this.rule = map.get(votable.getType());
	}
	
	public ReputationEvent reputationEvent() {
		if (shouldCountKarma) {
			EventType eventType = rule.eventType(type);
			return new ReputationEvent(eventType, eventContext, votable.getAuthor());
		}
		return new ReputationEvent(EventType.MASSIVE_VOTE_IGNORED, eventContext, votable.getAuthor());
	}
	
	
}
