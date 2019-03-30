package com.avojak.webapp.aws.p2.repository.model.project;

import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link Project}.
 */
public class ProjectTest {

	private P2Repository metadata;
	private ProjectVersion latestVersion;
	private List<ProjectVersion> snapshots;
	private List<ProjectVersion> releases;
	private String latestSnapshotUrl;
	private String latestReleaseUrl;
	private String genericUrl;

	@Before
	public void setup() throws URISyntaxException {
		metadata = new P2Repository("name", new URI("http://www.example.com"), false, System.currentTimeMillis(),
				new HashSet<>());
		latestVersion = new ProjectVersion(new ComparableVersion("1.0.0"), new Date());
		snapshots = new ArrayList<>();
		releases = new ArrayList<>();
		latestSnapshotUrl = "https://www.example.com/snapshots";
		latestReleaseUrl = "https://www.example.com/releases";
		genericUrl = "https://www.example.com";
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullMetadata() {
		new Project(null, snapshots, releases, latestVersion, latestSnapshotUrl, latestReleaseUrl, genericUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullSnapshots() {
		new Project(metadata, null, releases, latestVersion, latestSnapshotUrl, latestReleaseUrl, genericUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullReleases() {
		new Project(metadata, snapshots, null, latestVersion, latestSnapshotUrl, latestReleaseUrl, genericUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullLatestVersion() {
		new Project(metadata, snapshots, releases, null, latestSnapshotUrl, latestReleaseUrl, genericUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullLatestSnapshotUrl() {
		new Project(metadata, snapshots, releases, latestVersion, null, latestReleaseUrl, genericUrl);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyLatestSnapshotUrl() {
		new Project(metadata, snapshots, releases, latestVersion, " ", latestReleaseUrl, genericUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullLatestReleaseUrl() {
		new Project(metadata, snapshots, releases, latestVersion, latestSnapshotUrl, null, genericUrl);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyLatestReleaseUrl() {
		new Project(metadata, snapshots, releases, latestVersion, latestSnapshotUrl, " ", genericUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullGenericUrl() {
		new Project(metadata, snapshots, releases, latestVersion, latestSnapshotUrl, latestReleaseUrl, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyGenericUrl() {
		new Project(metadata, snapshots, releases, latestVersion, latestSnapshotUrl, latestReleaseUrl, " ");
	}

	@Test
	public void testGetters() {
		final Project project = new Project(metadata, snapshots, releases, latestVersion, latestSnapshotUrl,
				latestReleaseUrl, genericUrl);
		assertEquals(metadata, project.getMetadata());
		assertEquals(snapshots, project.getSnapshots());
		assertEquals(releases, project.getReleases());
		assertEquals(latestVersion, project.getLatestVersion());
		assertEquals(latestSnapshotUrl, project.getLatestSnapshotUrl());
		assertEquals(latestReleaseUrl, project.getLatestReleaseUrl());
		assertEquals(genericUrl, project.getGenericUrl());
	}

	@Test(expected = NullPointerException.class)
	public void testCompareTo_NullOtherProject() {
		new Project(metadata, snapshots, releases, latestVersion, latestSnapshotUrl, latestReleaseUrl, genericUrl)
				.compareTo(null);
	}

	@Test
	public void testCompareTo() throws URISyntaxException {
		final Project project1 = new Project(new P2Repository("AAA", new URI("http://www.example.com"), false,
				System.currentTimeMillis(), new HashSet<>()), snapshots, releases, latestVersion, latestSnapshotUrl,
				latestReleaseUrl, genericUrl);
		final Project project2 = new Project(new P2Repository("AAA", new URI("http://www.example.com"), false,
				System.currentTimeMillis(), new HashSet<>()), snapshots, releases, latestVersion, latestSnapshotUrl,
				latestReleaseUrl, genericUrl);
		final Project project3 = new Project(new P2Repository("BBB", new URI("http://www.example.com"), false,
				System.currentTimeMillis(), new HashSet<>()), snapshots, releases, latestVersion, latestSnapshotUrl,
				latestReleaseUrl, genericUrl);
		assertEquals(0, project1.compareTo(project2));
		assertTrue(project1.compareTo(project3) < 0);
		assertTrue(project3.compareTo(project1) > 0);
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(Project.class).verify();
	}

}
