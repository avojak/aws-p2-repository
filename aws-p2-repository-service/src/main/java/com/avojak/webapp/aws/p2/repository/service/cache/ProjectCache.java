package com.avojak.webapp.aws.p2.repository.service.cache;

import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Cache to hold projects.
 */
@Component
public class ProjectCache {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectCache.class);

	private final LoadingCache<Boolean, Map<String, Project>> cache;

	@Autowired
	public ProjectCache(final LoadingCacheFactory cacheFactory) {
		checkNotNull(cacheFactory, "cacheFactory cannot be null");
		cache = cacheFactory.create();
	}

	/**
	 * Gets the list of projects.
	 *
	 * @return The non-null, possibly empty {@link List} of {@link Project} objects.
	 */
	public List<Project> getProjects() {
		try {
			final Map<String, Project> projectMap = cache.get(Boolean.TRUE);
			final List<Project> projects = new ArrayList<>(projectMap.values());
			Collections.sort(projects);
			return projects;
		} catch (final ExecutionException e) {
			LOGGER.error("Error while retrieving projects from cache", e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * Gets a single project.
	 *
	 * @param name
	 * 		The project name. Cannot be null or empty.
	 *
	 * @return The non-null {@link Optional} of the {@link Project}.
	 */
	public Optional<Project> getProject(final String name) {
		checkNotNull(name, "name cannot be null");
		checkArgument(!name.trim().isEmpty(), "name cannot be empty");
		try {
			final Map<String, Project> projects = cache.get(Boolean.TRUE);
			return Optional.ofNullable(projects.get(name));
		} catch (final ExecutionException e) {
			LOGGER.error("Error while retrieving project from cache", e);
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
