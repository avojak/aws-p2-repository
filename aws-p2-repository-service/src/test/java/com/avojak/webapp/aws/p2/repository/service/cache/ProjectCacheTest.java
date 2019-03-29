package com.avojak.webapp.aws.p2.repository.service.cache;

import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.model.project.ProjectVersion;
import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import com.google.common.cache.LoadingCache;
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
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ProjectCache}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectCacheTest {

	@Mock
	private LoadingCacheFactory cacheFactory;

	@Mock
	private LoadingCache<Boolean, Map<String, Project>> cache;

	private ProjectCache projectCache;
	private Project project;

	@Before
	public void setup() throws URISyntaxException {
		when(cacheFactory.create()).thenReturn(cache);
		project = new Project(new P2Repository("Mock", new URI("https://www.example.com"), false,
				System.currentTimeMillis(), Collections.emptySet()), new ArrayList<>(), new ArrayList<>(),
				new ProjectVersion(new ComparableVersion("1.0.0"), new Date()), "https://www.example.com/snapshots",
				"https://www.example.com/releases", "https://www.example.com/");
		projectCache = new ProjectCache(cacheFactory);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullCacheFactory() {
		new ProjectCache(null);
	}

	@Test(expected = RuntimeException.class)
	public void testGetProjects_Error() throws ExecutionException {
		when(cache.get(Boolean.TRUE)).thenThrow(ExecutionException.class);
		projectCache.getProjects();
	}

	@Test
	public void testGetProjects() throws ExecutionException {
		final Map<String, Project> expectedProjectMap = Collections.singletonMap("mock", project);
		when(cache.get(Boolean.TRUE)).thenReturn(expectedProjectMap);
		assertEquals(Arrays.asList(project), projectCache.getProjects());
	}

	@Test(expected = NullPointerException.class)
	public void testGetProject_NullProjectName() {
		projectCache.getProject(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetProject_EmptyProjectName() {
		projectCache.getProject(" ");
	}

	@Test(expected = RuntimeException.class)
	public void testGetProject_Error() throws ExecutionException {
		when(cache.get(Boolean.TRUE)).thenThrow(ExecutionException.class);
		projectCache.getProject("mock");
	}

	@Test
	public void testGetProject() throws ExecutionException {
		final Map<String, Project> expectedProjectMap = Collections.singletonMap("mock", project);
		when(cache.get(Boolean.TRUE)).thenReturn(expectedProjectMap);

		final Optional<Project> result = projectCache.getProject("mock");

		assertTrue(result.isPresent());
		assertEquals(project, result.get());
	}

	@Test
	public void testGetProject_NoMatch() throws ExecutionException {
		final Map<String, Project> expectedProjectMap = Collections.singletonMap("mock", project);
		when(cache.get(Boolean.TRUE)).thenReturn(expectedProjectMap);

		final Optional<Project> result = projectCache.getProject("foobar");
		assertFalse(result.isPresent());
	}

}
