package org.learn.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.learn.dao.TestCase;
import org.learn.model.Flag;
import org.learn.model.FlagType;

public class FlagTest extends TestCase {

	@Test(expected=IllegalStateException.class)
	public void should_throw_exception_when_setting_reason_in_not_other_type() {
		Flag flag = flag(FlagType.OBSOLETE, user("author", "author@brutal.com"));
		flag.setReason("blabla");
	}

	@Test
	public void should_set_reason_in_flag_with_type_other() {
		Flag flag = flag(FlagType.OTHER, user("author", "author@brutal.com"));
		String reason = "blabla";
		flag.setReason(reason);
		assertEquals(reason, flag.getReason());
	}

}
