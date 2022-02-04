package org.learn.reputation.rules;

import org.learn.model.EventType;
import org.learn.model.VoteType;

public interface VotableRule {

	EventType eventType(VoteType type);

}
