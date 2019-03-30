package com.avojak.webapp.aws.p2.repository.webapp.controller;

import com.avojak.webapp.aws.p2.repository.model.Uptime;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.model.project.ProjectVersion;
import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import com.avojak.webapp.aws.p2.repository.service.DataService;
import com.avojak.webapp.aws.p2.repository.webapp.configuration.WebappProperties;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link ViewController}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ViewControllerTest {

	@Mock
	private DataService dataService;

	@Mock
	private Model model;

	private WebappProperties properties;
	private ViewController controller;

	@Before
	public void setup() {
		properties = new WebappProperties("Brand Name", "brand.icon", "brand.favicon", "custom.domain.com", "Welcome!",
				"my-bucket");
		controller = new ViewController(dataService, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullDataService() {
		new ViewController(null, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullWebappProperties() {
		new ViewController(dataService, null);
	}

	@Test
	public void testIndex() {
		when(dataService.getProjects()).thenReturn(new ArrayList<>());

		assertEquals("browse", controller.index(model));

		verify(model).addAttribute("title", "Brand Name");
		verify(model).addAttribute("brandIcon", "brand.icon");
		verify(model).addAttribute("brandFavicon", "brand.favicon");
		verify(model).addAttribute("welcomeMessage", "Welcome!");
		verify(model).addAttribute("projectCount", 0);
		verifyNoMoreInteractions(model);
	}

	@Test
	public void testShowProject() throws URISyntaxException {
		final Project project = new Project(
				new P2Repository("mock", new URI("https://p2.avojak.com/mock"), false, 23456789L, new HashSet<>()),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0-SNAPSHOT"), new Date(12345678L))),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0"), new Date(23456789L))),
				new ProjectVersion(new ComparableVersion("1.0.0"), new Date(23456789L)),
				"https://p2.avojak.com/content/mock/snapshots/latest",
				"https://p2.avojak.com/content/mock/releases/latest",
				"https://p2.avojak.com/content/mock/{snapshots|releases}/{version}");
		when(dataService.getProject("mock")).thenReturn(Optional.of(project));

		assertEquals("project", controller.showProject("mock", model));

		verify(model).addAttribute("project", project);
		verify(model).addAttribute("domain", "custom.domain.com");
		verifyNoMoreInteractions(model);
	}

	@Test
	public void testShowProject_ProjectDoesNotExist() {
		when(dataService.getProject("mock")).thenReturn(Optional.empty());

		try {
			controller.showProject("mock", model);
		} catch (final IllegalArgumentException e) {
			assertEquals("Project does not exist: mock", e.getMessage());
		}

		verifyZeroInteractions(model);
	}

	@Test
	public void testShowDashboard() throws URISyntaxException {
		final Project project = new Project(
				new P2Repository("mock", new URI("https://p2.avojak.com/mock"), false, 23456789L, new HashSet<>()),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0-SNAPSHOT"), new Date(12345678L))),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0"), new Date(23456789L))),
				new ProjectVersion(new ComparableVersion("1.0.0"), new Date(23456789L)),
				"https://p2.avojak.com/content/mock/snapshots/latest",
				"https://p2.avojak.com/content/mock/releases/latest",
				"https://p2.avojak.com/content/mock/{snapshots|releases}/{version}");
		final Uptime uptime = new Uptime(0, 1, 0);
		when(dataService.getProjects()).thenReturn(Arrays.asList(project));
		when(dataService.getUptime()).thenReturn(uptime);

		assertEquals("fragments/dashboard :: dashboard", controller.showDashboard(model));

		verify(model).addAttribute("projects", Arrays.asList(project));
		verify(model).addAttribute("domain", "custom.domain.com");
		verify(model).addAttribute("uptime", uptime);
		verify(model).addAttribute("bucket", "my-bucket");
		verifyNoMoreInteractions(model);
	}

	@Test
	public void testShowRecentUpdates() throws URISyntaxException {
		final Project project = new Project(
				new P2Repository("mock", new URI("https://p2.avojak.com/mock"), false, 23456789L, new HashSet<>()),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0-SNAPSHOT"), new Date(12345678L))),
				Arrays.asList(new ProjectVersion(new ComparableVersion("1.0.0"), new Date(23456789L))),
				new ProjectVersion(new ComparableVersion("1.0.0"), new Date(23456789L)),
				"https://p2.avojak.com/content/mock/snapshots/latest",
				"https://p2.avojak.com/content/mock/releases/latest",
				"https://p2.avojak.com/content/mock/{snapshots|releases}/{version}");
		when(dataService.getProjects()).thenReturn(Arrays.asList(project));

		assertEquals("fragments/projects :: projectList", controller.showRecentUpdates(model));

		verify(model).addAttribute("projects", Arrays.asList(project));
		verifyNoMoreInteractions(model);
	}

}
