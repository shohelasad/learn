package org.learn.controllers;

import java.util.List;

import javax.inject.Inject;

import br.com.caelum.vraptor.*;
import org.joda.time.DateTime;
import org.learn.brutauth.auth.rules.ModeratorOnlyRule;
import org.learn.dao.BlockedIpDao;
import org.learn.dao.VoteDAO;
import org.learn.dto.SuspectMassiveVote;
import org.learn.model.LoggedUser;
import org.learn.model.VoteType;
import org.learn.model.ban.BlockedIp;

import br.com.caelum.brutauth.auth.annotations.CustomBrutauthRules;
import br.com.caelum.vraptor.routes.annotation.Routed;

@Routed
@Controller
public class AntiHackController {

	@Inject private VoteDAO votes;
	@Inject private BlockedIpDao blockedIps;
	@Inject private Result result;
	@Inject private LoggedUser loggedUser;
	
	@Get
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void massiveVotesForm() {}
	
	@Post
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void showSuspects(DateTime begin, DateTime end, VoteType voteType) {
		List<SuspectMassiveVote> suspects = votes.suspectMassiveVote(voteType, begin, end);
		result.include("voteType", voteType.toString());
		result.include("startDate", begin);
		result.include("endDate", end);
		result.include("suspects", suspects);
	}

	@Get
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void ipBlockingForm() {
		List<BlockedIp> blockedIps = this.blockedIps.list();
		result.include("blockedIps", blockedIps);
	}

	@Post
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void newBlockedIp(BlockedIp blockedIp) {
		blockedIps.save(blockedIp);
		blockedIp.setAuthor(loggedUser.getCurrent());
		result.redirectTo(this).ipBlockingForm();
	}

	@Delete
	@CustomBrutauthRules(ModeratorOnlyRule.class)
	public void delete(Long id) {
		int count = blockedIps.delete(id);
		if (count == 0) {
			result.notFound();
			return;
		}
		result.redirectTo(this).ipBlockingForm();
	}
}
