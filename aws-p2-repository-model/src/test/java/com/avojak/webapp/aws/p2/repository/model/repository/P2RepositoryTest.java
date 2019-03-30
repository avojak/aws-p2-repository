package com.avojak.webapp.aws.p2.repository.model.repository;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link P2Repository}.
 */
public class P2RepositoryTest {

	private String name;
	private URI location;
	private boolean isCompressed;
	private long lastModified;
	private Set<IUGroup> groups;

	@Before
	public void setup() throws URISyntaxException {
		name = "Mock";
		location = new URI("https://www.example.com");
		isCompressed = false;
		lastModified = System.currentTimeMillis();
		groups = Collections.emptySet();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullName() {
		new P2Repository(null, location, isCompressed, lastModified, groups);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyName() {
		new P2Repository(" ", location, isCompressed, lastModified, groups);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullLocation() {
		new P2Repository(name, null, isCompressed, lastModified, groups);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullGroups() {
		new P2Repository(name, location, isCompressed, lastModified, null);
	}

	@Test
	public void testGetters() {
		final P2Repository repository = new P2Repository(name, location, isCompressed, lastModified, groups);
		assertEquals(name, repository.getName());
		assertEquals(location, repository.getLocation());
		assertEquals(isCompressed, repository.isCompressed());
		assertEquals(lastModified, repository.getLastModified());
		assertEquals(groups, repository.getGroups());
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(P2Repository.class).verify();
	}

}
