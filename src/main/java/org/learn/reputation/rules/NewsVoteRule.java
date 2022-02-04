package org.learn.reputation.rules;

import org.learn.model.EventType;
import org.learn.model.VoteType;

public class NewsVoteRule implements VotableRule {

	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? EventType.NEWS_UPVOTE : EventType.NEWS_DOWNVOTE;	}

}
