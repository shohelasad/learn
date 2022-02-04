package org.learn.reputation.rules;

import org.learn.model.EventType;
import org.learn.model.VoteType;

public class QuestionVoteRule implements VotableRule {

	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? EventType.QUESTION_UPVOTE : EventType.QUESTION_DOWNVOTE; 
	}

}
