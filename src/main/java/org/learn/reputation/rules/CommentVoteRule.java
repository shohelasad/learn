package org.learn.reputation.rules;

import org.learn.model.EventType;
import org.learn.model.VoteType;

public class CommentVoteRule implements VotableRule {


	@Override
	public EventType eventType(VoteType type) {
		if (type != VoteType.UP) {
			throw new IllegalArgumentException("comment cannot be downvoted");
		}
		return EventType.COMMENT_UPVOTE;
	}

}
