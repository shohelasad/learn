package org.learn.controllers;

import java.util.List;

import org.learn.model.Vote;
import org.learn.reputation.rules.KarmaCalculator;

public class RetrieveKarmaDownvote {
	public void retrieveKarma (List<Vote> votes) {
		for (Vote vote : votes) {
			if (vote.isDown()) {
				vote.getAuthor().descreaseKarma(KarmaCalculator.DOWNVOTED_QUESTION_OR_ANSWER);
			}
		}
	}
}
