package com.avojak.webapp.aws.p2.repository.webapp.controller;

import com.avojak.webapp.aws.p2.repository.model.Qualifier;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.service.DataService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link DataController}.
 */
@RunWith(MockitoJUnitRunner.class)
public class DataControllerTest {

	@Mock
	private DataService dataService;

	@Mock
	private HttpServletRequest request;

	private DataController controller;

	@Before
	public void setup() {
		controller = new DataController(dataService);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullDataService() {
		new DataController(null);
	}

	@Test
	public void testGetResource() {
		final String requestURI = "content/mock-project/releases/1.0.0";
		when(request.getRequestURI()).thenReturn(requestURI);
		final String project = "mock-project";
		final String qualifier = "releases";
		final String version = "1.0.0";
		final Resource resource = mock(Resource.class);
		when(dataService.getResource("mock-project/releases/1.0.0", project, Qualifier.RELEASE, version))
				.thenReturn(Optional.of(resource));

		final ResponseEntity<Resource> response = controller.getResource(project, qualifier, version, request);
		assertEquals(200, response.getStatusCodeValue());
		assertEquals(resource, response.getBody());
	}

	@Test
	public void testGetResource_NotFound() {
		final String requestURI = "content/mock-project/releases/1.0.0";
		when(request.getRequestURI()).thenReturn(requestURI);
		final String project = "mock-project";
		final String qualifier = "releases";
		final String version = "1.0.0";
		when(dataService.getResource("mock-project/releases/1.0.0", project, Qualifier.RELEASE, version))
				.thenReturn(Optional.empty());

		final ResponseEntity<Resource> response = controller.getResource(project, qualifier, version, request);
		assertEquals(404, response.getStatusCodeValue());
	}

	@Test
	public void testGetProjects() {
		final List<Project> projects = new ArrayList<>();
		when(dataService.getProjects()).thenReturn(projects);

		final ResponseEntity<Collection<Project>> response = controller.getProjects();

		assertEquals(200, response.getStatusCodeValue());
		assertEquals(projects, response.getBody());
	}

}
