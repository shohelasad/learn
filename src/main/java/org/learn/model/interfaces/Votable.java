package org.learn.model.interfaces;

import java.io.Serializable;
import java.util.List;

import org.learn.model.Question;
import org.learn.model.User;
import org.learn.model.Vote;

public interface Votable {
	void substitute(Vote previous, Vote current);
	void remove(Vote previous);
	User getAuthor();
	Serializable getId();
	long getVoteCount();
    Class<? extends Votable> getType();
	Question getQuestion();
	List<Vote> getVotes();
}
