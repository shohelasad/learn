package org.learn.model.flag;

import javax.inject.Inject;

import org.learn.model.Answer;
import org.learn.model.LoggedUser;
import org.learn.model.interfaces.Flaggable;

public class RemoveSolutionFlaggedByModerator implements FlagAction {
	
	private LoggedUser loggedUser;

	@Deprecated
	public RemoveSolutionFlaggedByModerator() {
	}

	@Inject
	public RemoveSolutionFlaggedByModerator(LoggedUser loggedUser) {
		this.loggedUser = loggedUser;
	}

	@Override
	public void fire(Flaggable flaggable) {
		Answer answer = (Answer) flaggable;
		if(answer.isSolution()){
			answer.uncheckAsSolution();
		}
	}
	
	@Override
	public boolean shouldHandle(Flaggable flaggable) {
		boolean isQuestion = Answer.class.isAssignableFrom(flaggable.getClass());
		return isQuestion && loggedUser.isLoggedIn() && loggedUser.isModerator();
	}

}
