package com.avojak.webapp.aws.p2.repository.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.S3BucketRepository;
import com.avojak.webapp.aws.p2.repository.model.Qualifier;
import com.avojak.webapp.aws.p2.repository.model.Uptime;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.service.DataService;
import com.avojak.webapp.aws.p2.repository.service.cache.ProjectCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link DataService}.
 */
@Service
public class DataServiceImpl implements DataService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataServiceImpl.class);
	private static final String LATEST_VERSION = "latest";

	private final S3BucketRepository repository;
	private final ProjectCache projectCache;
	private final ApplicationContext context;

	/**
	 * Constructor.
	 *
	 * @param repository
	 * 		The {@link S3BucketRepository}. Cannot be null.
	 * @param projectCache
	 * 		The {@link ProjectCache}. Cannot be null.
	 * @param context
	 * 		The {@link ApplicationContext}. Cannot be null.
	 */
	@Autowired
	public DataServiceImpl(final S3BucketRepository repository, final ProjectCache projectCache,
						   final ApplicationContext context) {
		this.repository = checkNotNull(repository, "repository cannot be null");
		this.projectCache = checkNotNull(projectCache, "projectCache cannot be null");
		this.context = checkNotNull(context, "context cannot be null");
	}

	@Override
	public Optional<Resource> getResource(final String key, final String project, final Qualifier qualifier,
										  final String version) {
		checkNotNull(key, "key cannot be null");
		checkArgument(!key.trim().isEmpty(), "key cannot be empty");
		checkNotNull(project, "project cannot be null");
		checkArgument(!project.trim().isEmpty(), "project cannot be empty");
		checkNotNull(qualifier, "qualifier cannot be null");
		checkNotNull(version, "version cannot be null");
		checkArgument(!version.trim().isEmpty(), "version cannot be empty");

		// TODO: It would be better to match the "latest" version in the REST controller
		if (LATEST_VERSION.equals(version)) {
			final Optional<String> latestVersion = getLatestVersion(project, qualifier);
			if (!latestVersion.isPresent()) {
				return Optional.empty();
			}
			final String latestKey = key.replace(LATEST_VERSION, latestVersion.get());
			LOGGER.debug("New key: {}", latestKey);
			return getResource(latestKey);
		}

		return getResource(key);
	}

	private Optional<String> getLatestVersion(final String name, final Qualifier qualifier) {
		final Optional<Project> project = projectCache.getProject(name);
		if (project.isPresent()) {
			switch (qualifier) {
				case SNAPSHOT:
					if (project.get().getSnapshots().isEmpty()) {
						return Optional.empty();
					}
					return Optional.of(project.get().getSnapshots().get(0).getVersion().toString());
				case RELEASE:
					if (project.get().getReleases().isEmpty()) {
						return Optional.empty();
					}
					return Optional.of(project.get().getReleases().get(0).getVersion().toString());
				default:
					throw new IllegalStateException("Unsupported version qualifier: " + qualifier.name());
			}
		}
		return Optional.empty();
	}

	private Optional<Resource> getResource(final String key) {
		final Optional<S3Object> object = repository.getObject(key);
		if (object.isPresent()) {
			return Optional.of(new InputStreamResource(object.get().getObjectContent()));
		}
		LOGGER.debug("No object found for key [{}]", key);
		return Optional.empty();
	}

	@Override
	public List<Project> getProjects() {
		return projectCache.getProjects();
	}

	@Override
	public Optional<Project> getProject(final String name) {
		return projectCache.getProject(name);
	}

	@Override
	public Uptime getUptime() {
		final Duration duration = Duration.ofMillis(System.currentTimeMillis() - context.getStartupDate());
		final long hours = duration.toHours();
		final long days = hours % 24;
		final int minutes = (int) ((duration.getSeconds() % 3600) / 60);
		return new Uptime(days, hours, minutes);
	}

}
