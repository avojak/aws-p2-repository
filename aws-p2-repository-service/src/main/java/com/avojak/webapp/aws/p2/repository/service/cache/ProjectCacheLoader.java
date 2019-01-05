package com.avojak.webapp.aws.p2.repository.service.cache;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.S3BucketRepository;
import com.avojak.webapp.aws.p2.repository.model.Qualifier;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.model.project.ProjectVersion;
import com.google.common.cache.CacheLoader;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of {@link CacheLoader} for the {@link ProjectCache}.
 */
@Component
public class ProjectCacheLoader extends CacheLoader<Boolean, Map<String, Project>> {

	@Autowired
	private S3BucketRepository repository;

	@Override
	public Map<String, Project> load(final Boolean k) {
		final List<S3ObjectSummary> summaries = repository.getObjectSummaries("")
				.stream()
				.filter(p -> p.getKey().contains("p2.index"))
				.collect(Collectors.toList());

		final Map<String, Project> projects = new HashMap<>();
		for (final S3ObjectSummary summary : summaries) {
			final String[] tokens = summary.getKey().split("/");
			final String name = tokens[0];
			final Qualifier qualifier = Qualifier.fromPathElement(tokens[1]);
			final ComparableVersion version = new ComparableVersion(tokens[2]);

			final List<ProjectVersion> snapshots = new ArrayList<>();
			final List<ProjectVersion> releases = new ArrayList<>();

			switch (qualifier) {
				case SNAPSHOT:
					snapshots.add(new ProjectVersion(version, summary.getLastModified()));
					break;
				case RELEASE:
					releases.add(new ProjectVersion(version, summary.getLastModified()));
					break;
				default:
					throw new IllegalStateException("Unsupported version qualifier: " + qualifier.name());
			}

			projects.put(name, new Project(name, snapshots, releases));
		}

		return projects;
	}

}
