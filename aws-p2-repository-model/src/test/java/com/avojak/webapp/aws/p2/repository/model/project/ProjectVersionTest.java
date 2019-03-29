package com.avojak.webapp.aws.p2.repository.model.project;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link ProjectVersion}.
 */
public class ProjectVersionTest {

	private ComparableVersion version;
	private Date date;

	@Before
	public void setup() {
		version = new ComparableVersion("1.0.0");
		date = new Date();
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullVersion() {
		new ProjectVersion(null, date);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullDate() {
		new ProjectVersion(version, null);
	}

	@Test
	public void testGetters() {
		final ProjectVersion projectVersion = new ProjectVersion(version, date);
		assertEquals(version, projectVersion.getVersion());
		assertEquals(date, projectVersion.getDate());
	}

	@Test
	public void testCompareTo() {
		final ProjectVersion projectVersion1 = new ProjectVersion(new ComparableVersion("1.0.0"), date);
		final ProjectVersion projectVersion2 = new ProjectVersion(new ComparableVersion("1.0.0"), date);
		final ProjectVersion projectVersion3 = new ProjectVersion(new ComparableVersion("2.0.0"), date);
		assertEquals(0, projectVersion1.compareTo(projectVersion2));
		assertTrue(projectVersion1.compareTo(projectVersion3) > 0);
		assertTrue(projectVersion3.compareTo(projectVersion1) < 0);
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(ProjectVersion.class).verify();
	}

}
