package org.learn.reputation.rules;

import org.learn.model.EventType;
import org.learn.model.ReputationEvent;
import org.learn.model.User;

public class MassiveVoteRevertEvent {

	private int karma;
	private final User target;
	
	public MassiveVoteRevertEvent(int karma, User target) {
		this.karma = karma;
		this.target = target;
	}
	
	public ReputationEvent reputationEvent() {
		EventType massiveVoteReverted = EventType.MASSIVE_VOTE_REVERTED;
		massiveVoteReverted.setKarma(karma);
		return new ReputationEvent(massiveVoteReverted, null, target);
	}
}
