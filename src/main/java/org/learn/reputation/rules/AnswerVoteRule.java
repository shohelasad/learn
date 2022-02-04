package org.learn.reputation.rules;

import static org.learn.model.EventType.ANSWER_DOWNVOTE;
import static org.learn.model.EventType.ANSWER_UPVOTE;

import org.learn.model.EventType;
import org.learn.model.VoteType;

public class AnswerVoteRule implements VotableRule {


	@Override
	public EventType eventType(VoteType type) {
		return type == VoteType.UP ? ANSWER_UPVOTE : ANSWER_DOWNVOTE;
	}

}
