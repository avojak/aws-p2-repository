package com.avojak.webapp.aws.p2.repository.service.impl;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.S3BucketRepository;
import com.avojak.webapp.aws.p2.repository.model.Qualifier;
import com.avojak.webapp.aws.p2.repository.model.Uptime;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.model.project.ProjectVersion;
import com.avojak.webapp.aws.p2.repository.service.DataService;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
	private final ApplicationContext context;

	/**
	 * Constructor.
	 *
	 * @param repository
	 * 		The {@link S3BucketRepository}. Cannot be null.
	 */
	@Autowired
	public DataServiceImpl(final S3BucketRepository repository, final ApplicationContext context) {
		this.repository = checkNotNull(repository, "repository cannot be null");
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

	private Optional<Resource> getResource(final String key) {
		final Optional<S3Object> object = repository.getObject(key);
		if (object.isPresent()) {
			return Optional.of(new InputStreamResource(object.get().getObjectContent()));
		}
		LOGGER.debug("No object found for key [{}]", key);
		return Optional.empty();
	}


	// TODO: Implement a cache in the data layer? This is horribly inefficient since we scan all objects every time...
	@Override
	public Optional<String> getLatestVersion(final String project, final Qualifier qualifier) {
		checkNotNull(project, "project cannot be null");
		checkArgument(!project.trim().isEmpty(), "project cannot be empty");
		checkNotNull(qualifier, "qualifier cannot be null");

		final String prefix = project + "/" + qualifier.getPathElement() + "/";
		final List<S3ObjectSummary> summaries = repository.getObjectSummaries(prefix);
		final Set<ComparableVersion> versions = new HashSet<>();
		for (final S3ObjectSummary summary : summaries) {
			final String key = summary.getKey();
			final String regex = prefix + ".+/";
			if (key.matches(regex)) {
				final String version = key.replace(prefix, "").split("/")[0];
				versions.add(new ComparableVersion(version));
			}
		}

		if (versions.isEmpty()) {
			LOGGER.debug("No latest {} version for {}", qualifier.toString(), project);
			return Optional.empty();
		}

		final List<ComparableVersion> comparableVersions = new ArrayList<>(versions);
		Collections.sort(comparableVersions);
		final String latestVersion = comparableVersions.get(comparableVersions.size() - 1).toString();
		LOGGER.debug("Latest {} version for {} is {}", qualifier.toString(), project, latestVersion);
		return Optional.of(latestVersion);
	}

	@Override
	public List<Project> getProjects() {
		final List<S3ObjectSummary> summaries = repository.getObjectSummaries("")
				.stream()
				.filter(p -> p.getKey().contains("p2.index"))
				.collect(Collectors.toList());

		final List<String> projectNames = new ArrayList<>();
		final Map<String, List<ProjectVersion>> snapshots = new HashMap<>();
		final Map<String, List<ProjectVersion>> releases = new HashMap<>();

		for (final S3ObjectSummary summary : summaries) {
			final String[] tokens = summary.getKey().split("/");
			final String name = tokens[0];
			final Qualifier qualifier = Qualifier.fromPathElement(tokens[1]);
			final ComparableVersion version = new ComparableVersion(tokens[2]);

			if (!projectNames.contains(name)) {
				projectNames.add(name);
				snapshots.put(name, new ArrayList<>());
				releases.put(name, new ArrayList<>());
			}

			switch (qualifier) {
				case SNAPSHOT:
					snapshots.get(name).add(new ProjectVersion(version, summary.getLastModified()));
					break;
				case RELEASE:
					releases.get(name).add(new ProjectVersion(version, summary.getLastModified()));
					break;
				default:
					throw new IllegalStateException("Unsupported qualifier: " + qualifier.name());
			}
		}

		final List<Project> projects = new ArrayList<>();
		for (final String projectName : projectNames) {
			projects.add(new Project(projectName, snapshots.get(projectName), releases.get(projectName)));
		}
		return projects;
	}

	@Override
	public Project getProject(final String name) {
		checkNotNull(name, "name cannot be null");
		checkArgument(!name.trim().isEmpty(), "name cannot be empty");

		final List<S3ObjectSummary> summaries = repository.getObjectSummaries(name)
				.stream()
				.filter(p -> p.getKey().contains("p2.index"))
				.collect(Collectors.toList());

		final List<ProjectVersion> snapshots = new ArrayList<>();
		final List<ProjectVersion> releases = new ArrayList<>();

		for (final S3ObjectSummary summary : summaries) {
			final String[] tokens = summary.getKey().split("/");
			final Qualifier qualifier = Qualifier.fromPathElement(tokens[1]);
			final ComparableVersion version = new ComparableVersion(tokens[2]);

			switch (qualifier) {
				case SNAPSHOT:
					snapshots.add(new ProjectVersion(version, summary.getLastModified()));
					break;
				case RELEASE:
					releases.add(new ProjectVersion(version, summary.getLastModified()));
					break;
				default:
					throw new IllegalStateException("Unsupported qualifier: " + qualifier.name());
			}
		}

		return new Project(name, snapshots, releases);
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
