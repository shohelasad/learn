package org.learn.ads;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.learn.ads.BrutalAds;
import org.learn.dao.TestCase;
import org.learn.model.LoggedUser;
import org.learn.model.User;

public class BrutalAdsTest extends TestCase{

	@Test
	public void should_return_true_if_user_have_less_then_50_karma() {
		User user = user("Leo", "leo@leo.com");
		user.increaseKarma(20);
		LoggedUser loggedUser = new LoggedUser(user, null);
		BrutalAds brutalAds = new BrutalAds(loggedUser);
		
		assertTrue(brutalAds.shouldShowAds());
	}

	@Test
	public void should_return_true_if_user_not_logged() {
		LoggedUser loggedUser = new LoggedUser(null, null);
		BrutalAds brutalAds = new BrutalAds(loggedUser);
		
		assertTrue(brutalAds.shouldShowAds());

	}
}