package com.avojak.webapp.aws.p2.repository.webapp.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link RedirectController}.
 */
public class RedirectControllerTest {

	@Test
	public void testRedirect() {
		assertEquals("redirect:/browse", new RedirectController().redirectToBrowse());
	}

}
