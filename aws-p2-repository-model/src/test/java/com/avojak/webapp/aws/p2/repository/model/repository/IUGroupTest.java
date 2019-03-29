package com.avojak.webapp.aws.p2.repository.model.repository;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link IUGroup}.
 */
public class IUGroupTest {

	private String name;
	private String id;
	private String description;
	private String copyright;
	private Collection<License> licenses;
	private String version;

	@Before
	public void setup() {
		name = "Mock IU Group";
		id = "mock.id";
		description = "Mock description.";
		copyright = "Mock Copyright v1";
		licenses = Collections.emptySet();
		version = "1.0.0";
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullName() {
		new IUGroup(null, id, description, copyright, licenses, version);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyName() {
		new IUGroup(" ", id, description, copyright, licenses, version);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullId() {
		new IUGroup(name, null, description, copyright, licenses, version);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyId() {
		new IUGroup(name, " ", description, copyright, licenses, version);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullDescription() {
		new IUGroup(name, id, null, copyright, licenses, version);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullCopyright() {
		new IUGroup(name, id, description, null, licenses, version);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullLicenses() {
		new IUGroup(name, id, description, copyright, null, version);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullVersion() {
		new IUGroup(name, id, description, copyright, licenses, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyVersion() {
		new IUGroup(name, id, description, copyright, licenses, " ");
	}

	@Test
	public void testGetters() {
		final IUGroup group = new IUGroup(name, id, description, copyright, licenses, version);
		assertEquals(name, group.getName());
		assertEquals(id, group.getId());
		assertEquals(description, group.getDescription());
		assertEquals(copyright, group.getCopyright());
		assertEquals(licenses, group.getLicenses());
		assertEquals(version, group.getVersion());
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(IUGroup.class).verify();
	}

}
