package com.avojak.webapp.aws.p2.repository.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.S3BucketRepository;
import com.avojak.webapp.aws.p2.repository.model.Qualifier;
import com.avojak.webapp.aws.p2.repository.model.Uptime;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.model.project.ProjectVersion;
import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import com.avojak.webapp.aws.p2.repository.service.cache.ProjectCache;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link DataServiceImpl}
 */
@RunWith(MockitoJUnitRunner.class)
public class DataServiceImplTest {

	@Mock
	private S3BucketRepository repository;

	@Mock
	private ProjectCache cache;

	@Mock
	private InputStreamResourceFactory inputStreamResourceFactory;

	@Mock
	private ApplicationContext context;

	private DataServiceImpl service;

	@Before
	public void setup() {
		service = new DataServiceImpl(repository, cache, inputStreamResourceFactory, context);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullRepository() {
		new DataServiceImpl(null, cache, inputStreamResourceFactory, context);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullProjectCache() {
		new DataServiceImpl(repository, null, inputStreamResourceFactory, context);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullInputStreamResourceFactory() {
		new DataServiceImpl(repository, cache, null, context);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullContext() {
		new DataServiceImpl(repository, cache, inputStreamResourceFactory, null);
	}

	@Test(expected = NullPointerException.class)
	public void testGetResource_NullKey() {
		service.getResource(null, "project", Qualifier.RELEASE, "1.0.0");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetResource_EmptyKey() {
		service.getResource(" ", "project", Qualifier.RELEASE, "1.0.0");
	}

	@Test(expected = NullPointerException.class)
	public void testGetResource_NullProject() {
		service.getResource("key", null, Qualifier.RELEASE, "1.0.0");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetResource_EmptyProject() {
		service.getResource("key", " ", Qualifier.RELEASE, "1.0.0");
	}

	@Test(expected = NullPointerException.class)
	public void testGetResource_NullQualifier() {
		service.getResource("key", "project", null, "1.0.0");
	}

	@Test(expected = NullPointerException.class)
	public void testGetResource_NullVersion() {
		service.getResource("key", "project", Qualifier.RELEASE, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetResource_EmptyVersion() {
		service.getResource("key", "project", Qualifier.RELEASE, " ");
	}

	@Test
	public void testGetResource_LatestSnapshot() throws URISyntaxException {
		final Project project = new Project(new P2Repository("mock", new URI("https://www.example.com"), false,
				System.currentTimeMillis(), Collections.emptySet()),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0-SNAPSHOT"), new Date(123456L)),
						new ProjectVersion(new ComparableVersion("2.0.0-SNAPSHOT"), new Date(234567L))),
				new ArrayList<>(), new ProjectVersion(new ComparableVersion("2.0.0-SNAPSHOT"), new Date(234567L)),
				"https://www.example.com/snapshots", "https://www.example.com/releases", "https://www.example.com/");
		when(cache.getProject("project")).thenReturn(Optional.of(project));
		final S3Object s3Object = mock(S3Object.class);
		when(repository.getObject("key")).thenReturn(Optional.of(s3Object));
		final S3ObjectInputStream s3ObjectContent = mock(S3ObjectInputStream.class);
		when(s3Object.getObjectContent()).thenReturn(s3ObjectContent);
		final InputStreamResource inputStreamResource = mock(InputStreamResource.class);
		when(inputStreamResourceFactory.create(s3ObjectContent)).thenReturn(inputStreamResource);

		final Optional<Resource> resource = service.getResource("key", "project", Qualifier.SNAPSHOT, "latest");
		assertTrue(resource.isPresent());
		assertEquals(inputStreamResource, resource.get());
	}

	@Test
	public void testGetResource_NoLatestSnapshot() throws URISyntaxException {
		final Project project = new Project(new P2Repository("mock", new URI("https://www.example.com"), false,
				System.currentTimeMillis(), Collections.emptySet()),
				new ArrayList<>(), Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0"), new Date(123456L)),
				new ProjectVersion(new ComparableVersion("2.0.0"), new Date(234567L))),
				new ProjectVersion(new ComparableVersion("2.0.0"), new Date(234567L)),
				"https://www.example.com/snapshots", "https://www.example.com/releases", "https://www.example.com/");
		when(cache.getProject("project")).thenReturn(Optional.of(project));

		final Optional<Resource> resource = service.getResource("key", "project", Qualifier.SNAPSHOT, "latest");
		assertFalse(resource.isPresent());
	}

	@Test
	public void testGetResource_LatestRelease() throws URISyntaxException {
		final Project project = new Project(new P2Repository("mock", new URI("https://www.example.com"), false,
				System.currentTimeMillis(), Collections.emptySet()),
				new ArrayList<>(), Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0"), new Date(123456L)),
				new ProjectVersion(new ComparableVersion("2.0.0"), new Date(234567L))),
				new ProjectVersion(new ComparableVersion("2.0.0"), new Date(234567L)),
				"https://www.example.com/snapshots", "https://www.example.com/releases", "https://www.example.com/");
		when(cache.getProject("project")).thenReturn(Optional.of(project));
		final S3Object s3Object = mock(S3Object.class);
		when(repository.getObject("key")).thenReturn(Optional.of(s3Object));
		final S3ObjectInputStream s3ObjectContent = mock(S3ObjectInputStream.class);
		when(s3Object.getObjectContent()).thenReturn(s3ObjectContent);
		final InputStreamResource inputStreamResource = mock(InputStreamResource.class);
		when(inputStreamResourceFactory.create(s3ObjectContent)).thenReturn(inputStreamResource);

		final Optional<Resource> resource = service.getResource("key", "project", Qualifier.RELEASE, "latest");
		assertTrue(resource.isPresent());
		assertEquals(inputStreamResource, resource.get());
	}

	@Test
	public void testGetResource_NoLatestRelease() throws URISyntaxException {
		final Project project = new Project(new P2Repository("mock", new URI("https://www.example.com"), false,
				System.currentTimeMillis(), Collections.emptySet()),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0-SNAPSHOT"), new Date(123456L)),
						new ProjectVersion(new ComparableVersion("2.0.0-SNAPSHOT"), new Date(234567L))),
				new ArrayList<>(), new ProjectVersion(new ComparableVersion("2.0.0-SNAPSHOT"), new Date(234567L)),
				"https://www.example.com/snapshots", "https://www.example.com/releases", "https://www.example.com/");
		when(cache.getProject("project")).thenReturn(Optional.of(project));

		final Optional<Resource> resource = service.getResource("key", "project", Qualifier.RELEASE, "latest");
		assertFalse(resource.isPresent());
	}

	@Test
	public void testGetResource_Snapshot() {
		final S3Object s3Object = mock(S3Object.class);
		when(repository.getObject("key")).thenReturn(Optional.of(s3Object));
		final S3ObjectInputStream s3ObjectContent = mock(S3ObjectInputStream.class);
		when(s3Object.getObjectContent()).thenReturn(s3ObjectContent);
		final InputStreamResource inputStreamResource = mock(InputStreamResource.class);
		when(inputStreamResourceFactory.create(s3ObjectContent)).thenReturn(inputStreamResource);

		final Optional<Resource> resource = service.getResource("key", "project", Qualifier.SNAPSHOT, "1.0.0-SNAPSHOT");
		assertTrue(resource.isPresent());
		assertEquals(inputStreamResource, resource.get());
	}

	@Test
	public void testGetResource_SnapshotNotFound() {
		when(repository.getObject("key")).thenReturn(Optional.empty());

		final Optional<Resource> resource = service.getResource("key", "project", Qualifier.SNAPSHOT, "1.0.0-SNAPSHOT");
		assertFalse(resource.isPresent());
	}

	@Test
	public void testGetResource_Release() {
		final S3Object s3Object = mock(S3Object.class);
		when(repository.getObject("key")).thenReturn(Optional.of(s3Object));
		final S3ObjectInputStream s3ObjectContent = mock(S3ObjectInputStream.class);
		when(s3Object.getObjectContent()).thenReturn(s3ObjectContent);
		final InputStreamResource inputStreamResource = mock(InputStreamResource.class);
		when(inputStreamResourceFactory.create(s3ObjectContent)).thenReturn(inputStreamResource);

		final Optional<Resource> resource = service.getResource("key", "project", Qualifier.RELEASE, "1.0.0");
		assertTrue(resource.isPresent());
		assertEquals(inputStreamResource, resource.get());
	}

	@Test
	public void testGetResource_ReleaseNotFound() {
		when(repository.getObject("key")).thenReturn(Optional.empty());

		final Optional<Resource> resource = service.getResource("key", "project", Qualifier.RELEASE, "1.0.0");
		assertFalse(resource.isPresent());
	}

	@Test
	public void testGetProjects() throws URISyntaxException {
		final Project project = new Project(new P2Repository("mock", new URI("https://www.example.com"), false,
				System.currentTimeMillis(), Collections.emptySet()),
				new ArrayList<>(), Arrays.asList(new ProjectVersion(new ComparableVersion("2.0.0"), new Date(234567L))),
				new ProjectVersion(new ComparableVersion("2.0.0"), new Date(234567L)),
				"https://www.example.com/snapshots", "https://www.example.com/releases", "https://www.example.com/");
		when(cache.getProjects()).thenReturn(Arrays.asList(project));

		assertEquals(Arrays.asList(project), service.getProjects());
	}

	@Test
	public void testGetProject() throws URISyntaxException {
		final Project project = new Project(new P2Repository("mock", new URI("https://www.example.com"), false,
				System.currentTimeMillis(), Collections.emptySet()),
				new ArrayList<>(), Arrays.asList(new ProjectVersion(new ComparableVersion("2.0.0"), new Date(234567L))),
				new ProjectVersion(new ComparableVersion("2.0.0"), new Date(234567L)),
				"https://www.example.com/snapshots", "https://www.example.com/releases", "https://www.example.com/");
		when(cache.getProject("mock")).thenReturn(Optional.of(project));

		assertTrue(service.getProject("mock").isPresent());
		assertEquals(project, service.getProject("mock").get());
	}

	@Test
	public void testGetUptime() {
		when(context.getStartupDate()).thenReturn(System.currentTimeMillis());
		final Uptime expected = new Uptime(0, 0, 0);
		assertEquals(expected, service.getUptime());
	}

}
