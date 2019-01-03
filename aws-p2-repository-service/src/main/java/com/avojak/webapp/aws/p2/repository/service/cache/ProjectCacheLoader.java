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
import java.util.List;

@Component
public class ProjectCacheLoader extends CacheLoader<String, Project> {

	@Autowired
	private S3BucketRepository repository;

	@Override
	public Project load(final String projectName) throws Exception {
		final List<S3ObjectSummary> summaries = repository.getObjectSummaries(projectName);
		if (summaries.isEmpty()) {
			throw new RuntimeException("Project not found: " + projectName);
		}
		final List<ProjectVersion> snapshots = new ArrayList<>();
		final List<ProjectVersion> releases = new ArrayList<>();
		for (final S3ObjectSummary summary : summaries) {
			final String[] tokens = summary.getKey().replace(projectName, "").split("/");
			final Qualifier qualifier = Qualifier.fromPathElement(tokens[0]);
			final ComparableVersion version = new ComparableVersion(tokens[1]);
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
		}
		return new Project(projectName, snapshots, releases);
	}

}
