package com.avojak.webapp.aws.p2.repository.service.cache;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.MetadataRepository;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.S3BucketRepository;
import com.avojak.webapp.aws.p2.repository.model.Qualifier;
import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.model.project.ProjectVersion;
import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import com.avojak.webapp.aws.p2.repository.service.configuration.ServiceProperties;
import com.google.common.cache.CacheLoader;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
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
	private S3BucketRepository s3BucketRepository;

	@Autowired
	private MetadataRepository metadataRepository;

	@Autowired
	private ServiceProperties properties;

	// TODO: Refactor this
	@Override
	public Map<String, Project> load(final Boolean k) {
		final List<S3ObjectSummary> summaries = s3BucketRepository.getObjectSummaries("")
				.stream()
				.filter(p -> p.getKey().contains("p2.index"))
				.collect(Collectors.toList());

		final Map<String, List<S3ObjectSummary>> summariesByProject = new HashMap<>();
		for (final S3ObjectSummary summary : summaries) {
			final String name = summary.getKey().split("/")[0];
			if (!summariesByProject.containsKey(name)) {
				summariesByProject.put(name, new ArrayList<>());
			}
			summariesByProject.get(name).add(summary);
		}

		// Iterate over all projects
		final Map<String, Project> projects = new HashMap<>();
		for (final Map.Entry<String, List<S3ObjectSummary>> entry : summariesByProject.entrySet()) {

			final String name = entry.getKey();
			final List<ProjectVersion> snapshots = new ArrayList<>();
			final List<ProjectVersion> releases = new ArrayList<>();

			// Iterate over each version of the project
			S3ObjectSummary mostRecentSummary = null;
			ProjectVersion mostRecentVersion = null;
			for (final S3ObjectSummary summary : entry.getValue()) {
				final String[] objectKeyTokens = summary.getKey().split("/");
				final Qualifier qualifier = Qualifier.fromPathElement(objectKeyTokens[1]);
				final ProjectVersion version = new ProjectVersion(new ComparableVersion(objectKeyTokens[2]), summary.getLastModified());
				switch (qualifier) {
					case SNAPSHOT:
						snapshots.add(version);
						break;
					case RELEASE:
						releases.add(version);
						break;
					default:
						throw new IllegalStateException("Unsupported version qualifier: " + qualifier.name());
				}

				// Update the most recent summary and version
				if (mostRecentSummary == null) {
					mostRecentSummary = summary;
					mostRecentVersion = version;
				} else if (version.compareTo(mostRecentVersion) > 1) {
					mostRecentSummary = summary;
					mostRecentVersion = version;
				}
			}

			// Order the versions
			Collections.sort(snapshots);
			Collections.sort(releases);

			final P2Repository metadata = metadataRepository
					.getMetadata(s3BucketRepository.getHostingUrl(mostRecentSummary.getKey().replace("/p2.index", "")));
			// Override the name with the project name
			final P2Repository updatedMetadata = new P2Repository(name, metadata.getLocation(), metadata.isCompressed(),
					metadata.getLastModified(), metadata.getGroups());
			final String latestSnapshotUrl = String.format(properties.getLatestSnapshotContentUrlFormat(), properties.getCustomDomain(), name);
			final String latestReleaseUrl = String.format(properties.getLatestReleaseContentUrlFormat(), properties.getCustomDomain(), name);
			final String genericUrl = String.format(properties.getGenericContentUrlFormat(), properties.getCustomDomain(), name);
			projects.put(name, new Project(updatedMetadata, snapshots, releases, mostRecentVersion, latestSnapshotUrl,
					latestReleaseUrl, genericUrl));
		}

		return projects;
	}

}
