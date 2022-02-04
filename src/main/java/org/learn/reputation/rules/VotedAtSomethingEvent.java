package org.learn.reputation.rules;

import org.learn.model.EventType;
import org.learn.model.ReputationEvent;
import org.learn.model.ReputationEventContext;
import org.learn.model.Vote;

public class VotedAtSomethingEvent {
	
	private final Vote vote;
	private final ReputationEventContext eventContext;

	public VotedAtSomethingEvent(Vote vote, ReputationEventContext eventContext) {
		this.vote = vote;
		this.eventContext = eventContext;
	}

	public int reward() {
		if (vote.isDown()) {
			return KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER;
		}
		return 0;
	}

	public ReputationEvent reputationEvent() {
		ReputationEvent downvoted = new ReputationEvent(EventType.DOWNVOTED_SOMETHING, eventContext, vote.getAuthor());
		return vote.isDown() ? downvoted : ReputationEvent.IGNORED_EVENT;
	}

}
