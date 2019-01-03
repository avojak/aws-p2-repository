package com.avojak.webapp.aws.p2.repository.webapp.controller;

import com.avojak.webapp.aws.p2.repository.model.Qualifier;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Provides endpoints to retrieve data from the application.
 */
@RestController
@RequestMapping("content")
public class DataController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataController.class);

	private static final String REQUEST_URI_PREFIX = "content/";

	private final DataService service;

	/**
	 * Constructor.
	 *
	 * @param service
	 * 		The {@link DataService}.
	 */
	@Autowired
	public DataController(final DataService service) {
		this.service = checkNotNull(service, "service cannot be null");
	}

	/**
	 * Gets a resource from the repository.
	 *
	 * @param project
	 * 		The project name.
	 * @param qualifier
	 * 		The version {@link Qualifier} path element.
	 * @param version
	 * 		The version.
	 * @param request
	 * 		The {@link HttpServletRequest}.
	 *
	 * @return The non-null {@link ResponseEntity} containing the requested object.
	 */
	@GetMapping(value = "/{project}/{qualifier}/{version}/**", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public ResponseEntity<Resource> getResource(@PathVariable("project") final String project,
												@PathVariable("qualifier") final String qualifier,
												@PathVariable("version") final String version,
												final HttpServletRequest request) {
		final String requestURI = request.getRequestURI();
		final String key = StringUtils.substringAfter(requestURI, REQUEST_URI_PREFIX);
		LOGGER.debug("Parsed request URI [{}] into key [{}]", requestURI, key);

		final Optional<Resource> resource = service.getResource(key, project, Qualifier.fromPathElement(qualifier), version);
		return resource.map(r -> ResponseEntity.ok().body(r)).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Gets the latest version for a project.
	 *
	 * @param project
	 * 		The project name.
	 * @param qualifier
	 * 		The version {@link Qualifier} path element.
	 *
	 * @return The non-null {@link ResponseEntity} containing the latest version.
	 */
	@GetMapping(value = "/{project}/{qualifier}/latestVersion", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<String> getLatestVersion(@PathVariable("project") final String project,
												   @PathVariable("qualifier") final String qualifier) {
		final Optional<String> latestVersion = service.getLatestVersion(project, Qualifier.fromPathElement(qualifier));
		return latestVersion.map(v -> ResponseEntity.ok().body(v)).orElseGet(() -> ResponseEntity.notFound().build());
	}

	/**
	 * Gets the available projects in the repository.
	 *
	 * @return The non-null, possibly empty collection of projects.
	 */
	@GetMapping(value = "/projects", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Collection<Project>> getProjects() {
		return ResponseEntity.ok().body(service.getProjects());
	}

}
