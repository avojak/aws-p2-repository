package com.avojak.webapp.aws.p2.repository.service;

import com.avojak.webapp.aws.p2.repository.model.Qualifier;
import com.avojak.webapp.aws.p2.repository.model.Uptime;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;

/**
 * Provides methods to retrieve data.
 */
public interface DataService {

	/**
	 * Gets the resource for the given key.
	 *
	 * @param key
	 * 		The key of the resource. Cannot be null or empty.
	 *
	 * @return The {@link Optional} of the {@link Resource}.
	 */
	Optional<Resource> getResource(final String key, final String project, final Qualifier qualifier,
								   final String version);

	/**
	 * Gets the list of available projects.
	 *
	 * @return The non-null, possibly empty list of {@link Project} objects.
	 */
	List<Project> getProjects();

	/**
	 * Gets a project.
	 *
	 * @param name
	 * 		The project name. Cannot be null or empty.
	 *
	 * @return The non-null {@lnk Optional} of the {@link Project}.
	 */
	Optional<Project> getProject(final String name);

	/**
	 * Gets the application up-time.
	 *
	 * @return The non-null {@link Uptime}.
	 */
	Uptime getUptime();

}
