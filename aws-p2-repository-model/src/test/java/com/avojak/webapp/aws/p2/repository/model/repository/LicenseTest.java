package com.avojak.webapp.aws.p2.repository.model.repository;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link License}.
 */
public class LicenseTest {

	private String name;
	private String body;
	private URI location;

	@Before
	public void setup() throws URISyntaxException {
		name = "Mock License v1";
		body = "Mock license.";
		location = new URI("https://www.example.com");
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullName() {
		new License(null, body, location);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyName() {
		new License(" ", body, location);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullBody() {
		new License(name, null, location);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyBody() {
		new License(name, " ", location);
	}

	@Test
	public void testGetters() {
		final License license = new License(name, body, location);
		assertEquals(name, license.getName());
		assertEquals(body, license.getBody());
		assertEquals(location, license.getLocation());
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(License.class).verify();
	}

}
