package org.learn.controllers;

import javax.inject.Inject;

import org.learn.auth.rules.PermissionRules;
import org.learn.brutauth.auth.rules.EnvironmentKarmaRule;
import org.learn.dao.VoteDAO;
import org.learn.infra.ModelUrlMapping;
import org.learn.model.LoggedUser;
import org.learn.model.Vote;
import org.learn.model.VoteType;
import org.learn.model.interfaces.Votable;
import org.learn.model.vote.VotingMachine;

import br.com.caelum.brutauth.auth.annotations.SimpleBrutauthRules;
import br.com.caelum.vraptor.Controller;
import br.com.caelum.vraptor.Post;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.routes.annotation.Routed;
import br.com.caelum.vraptor.view.Results;

@Routed
@Controller
public class VoteController {

	@Inject private Result result;
	@Inject private LoggedUser currentUser;
	@Inject private VoteDAO votes;
	@Inject private VotingMachine votingMachine;
	@Inject private ModelUrlMapping mapping;
	@Inject private LoggedUser loggedUser;
	
	@Post
	@EnvironmentAccessLevel(PermissionRules.VOTE_UP)
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	public void voteUp(Long id, String type) {
		tryToVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
		loggedUser.getCurrent().votedUp();
	}

	@Post
	@EnvironmentAccessLevel(PermissionRules.VOTE_DOWN)
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	public void voteDown(Long id, String type) {
		tryToVoteVotable(id, VoteType.DOWN, mapping.getClassFor(type));
		
	}
	
	@Post
	@EnvironmentAccessLevel(PermissionRules.VOTE_UP)
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	public void voteUpRemoval(Long id, String type) {
		tryToRemoveVoteVotable(id, VoteType.UP, mapping.getClassFor(type));
		loggedUser.getCurrent().votedUp();
	}

	@Post
	@EnvironmentAccessLevel(PermissionRules.VOTE_DOWN)
	@SimpleBrutauthRules({EnvironmentKarmaRule.class})
	public void voteDownRemoval(Long id, String type) {
		tryToRemoveVoteVotable(id, VoteType.DOWN, mapping.getClassFor(type));
		
	}

	@SuppressWarnings("rawtypes")
	private void tryToVoteVotable(Long id, VoteType voteType, Class votableType) {
		try {
		    Votable votable = votes.loadVotable(votableType, id);
		    Vote current = new Vote(currentUser.getCurrent(), voteType);
		    votingMachine.register(votable, current, votableType);
		    votes.save(current);
		    result.use(Results.json()).withoutRoot().from(votable.getVoteCount()).serialize();
		} catch (IllegalArgumentException e) {
		    result.use(Results.http()).sendError(409);
		    return;
        }
	}
	
	@SuppressWarnings("rawtypes")
	private void tryToRemoveVoteVotable(Long id, VoteType voteType, Class votableType) {
		try {
		    Votable votable = votes.loadVotable(votableType, id);
		    Vote current = new Vote(currentUser.getCurrent(), voteType);
		    votingMachine.unRegister(votable, current, votableType);
//		    votes.save(current);
		    result.use(Results.json()).withoutRoot().from(votable.getVoteCount()).serialize();
		} catch (IllegalArgumentException e) {
		    result.use(Results.http()).sendError(409);
		    return;
        }
	}
}