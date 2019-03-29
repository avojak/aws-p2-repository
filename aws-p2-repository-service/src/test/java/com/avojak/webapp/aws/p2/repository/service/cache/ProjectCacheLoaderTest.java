package com.avojak.webapp.aws.p2.repository.service.cache;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.MetadataRepository;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.S3BucketRepository;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.model.project.ProjectVersion;
import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import com.avojak.webapp.aws.p2.repository.service.configuration.ServiceProperties;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ProjectCacheLoader}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectCacheLoaderTest {

	@Mock
	private S3BucketRepository bucketRepository;

	@Mock
	private MetadataRepository metadataRepository;

	private ServiceProperties properties;
	private ProjectCacheLoader cacheLoader;

	@Before
	public void setup() {
		properties = new ServiceProperties(30000L, TimeUnit.SECONDS, "https://%s/content/%s/snapshots/latest",
				"https://%s/content/%s/releases/latest", "https://%s/content/%s/{snapshots|releases}/{version}",
				"p2.avojak.com");
		cacheLoader = new ProjectCacheLoader(bucketRepository, metadataRepository, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullBucketRepository() {
		new ProjectCacheLoader(null, metadataRepository, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullMetadataRepository() {
		new ProjectCacheLoader(bucketRepository, null, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullProperties() {
		new ProjectCacheLoader(bucketRepository, metadataRepository, null);
	}

	@Test
	public void testLoad_NoProjects() {
		when(bucketRepository.getObjectSummaries("")).thenReturn(new ArrayList<>());
		assertTrue(cacheLoader.load(Boolean.TRUE).isEmpty());
	}

	@Test
	public void testLoad() throws URISyntaxException {
		final S3ObjectSummary snapshotObjectSummary = mock(S3ObjectSummary.class);
		final S3ObjectSummary releaseObjectSummary = mock(S3ObjectSummary.class);
		when(snapshotObjectSummary.getKey()).thenReturn("mock/snapshots/1.0.0-SNAPSHOT/p2.index");
		when(snapshotObjectSummary.getLastModified()).thenReturn(new Date(12345678L));
		when(releaseObjectSummary.getKey()).thenReturn("mock/releases/1.0.0/p2.index");
		when(releaseObjectSummary.getLastModified()).thenReturn(new Date(23456789L));
		when(bucketRepository.getObjectSummaries("")).thenReturn(Arrays.asList(snapshotObjectSummary, releaseObjectSummary));

		when(bucketRepository.getHostingUrl("mock/releases/1.0.0")).thenReturn("http://s3.amazonaws.com/p2.avojak.com/mock/releases/1.0.0");

		final P2Repository metadata = new P2Repository("wrong-name", new URI("https://p2.avojak.com/mock"), false, 23456789L, new HashSet<>());
		when(metadataRepository.getMetadata("http://s3.amazonaws.com/p2.avojak.com/mock/releases/1.0.0")).thenReturn(metadata);

		final Map<String, Project> expected = new HashMap<>();
		expected.put("mock", new Project(
				new P2Repository("mock", new URI("https://p2.avojak.com/mock"), false, 23456789L, new HashSet<>()),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0-SNAPSHOT"), new Date(12345678L))),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0"), new Date(23456789L))),
				new ProjectVersion(new ComparableVersion("1.0.0"), new Date(23456789L)),
				"https://p2.avojak.com/content/mock/snapshots/latest",
				"https://p2.avojak.com/content/mock/releases/latest",
				"https://p2.avojak.com/content/mock/{snapshots|releases}/{version}"));

		final Map<String, Project> actual = cacheLoader.load(Boolean.TRUE);

		assertEquals(expected, actual);
	}

}
