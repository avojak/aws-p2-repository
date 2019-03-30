package com.avojak.webapp.aws.p2.repository.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link Uptime}.
 */
public class UptimeTest {

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegativeDays() {
		new Uptime(-1, 2, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegativeHours() {
		new Uptime(1, -2, 3);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegativeMinutes() {
		new Uptime(1, 2, -3);
	}

	@Test
	public void testGetters() {
		final Uptime uptime = new Uptime(1, 2, 3);
		assertEquals(1, uptime.getDays());
		assertEquals(2, uptime.getHours());
		assertEquals(3, uptime.getMinutes());
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Uptime.class).verify();
	}

}
