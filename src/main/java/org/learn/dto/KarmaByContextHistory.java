package org.learn.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.joda.time.DateTime;
import org.learn.model.ReputationEventContext;

public class KarmaByContextHistory {

	private List<KarmaAndContext> history = new ArrayList<>();

	public KarmaByContextHistory(Collection<Object[]> results) {
		for (Object[] entry : results) {
			ReputationEventContext context = (ReputationEventContext) entry[0];
			Long karma = (Long) entry[1];
			DateTime date = (DateTime) entry[2];
			if(context.isVisible())
				history.add(new KarmaAndContext(context, karma, date));
		}
	}
	
	public List<KarmaAndContext> getHistory() {
		return history;
	}
}
