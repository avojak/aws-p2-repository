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
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link CacheLoader} for the {@link ProjectCache}.
 */
@Component
public class ProjectCacheLoader extends CacheLoader<Boolean, Map<String, Project>> {

	private static final String KEY_DELIM = "/";
	private static final String P2_INDEX_KEY_ELEM = "p2.index";

	private final S3BucketRepository s3BucketRepository;
	private final MetadataRepository metadataRepository;
	private final ServiceProperties properties;

	/**
	 * Constructor.
	 *
	 * @param s3BucketRepository
	 * 		The {@link S3BucketRepository}. Cannot be null.
	 * @param metadataRepository
	 * 		The {@link MetadataRepository}. Cannot be null.
	 * @param properties
	 * 		The {@link ServiceProperties}. Cannot be null.
	 */
	@Autowired
	public ProjectCacheLoader(final S3BucketRepository s3BucketRepository, final MetadataRepository metadataRepository,
							  final ServiceProperties properties) {
		this.s3BucketRepository = checkNotNull(s3BucketRepository, "s3BucketRepository cannot be null");
		this.metadataRepository = checkNotNull(metadataRepository, "metadataRepository cannot be null");
		this.properties = checkNotNull(properties, "properties cannot be null");
	}

	// TODO: Refactor this
	@Override
	public Map<String, Project> load(final Boolean k) {
		// Get the object summaries for every object where the key contains "p2.index.".
		// The p2.index file should be present once in every repository, so we use it as
		// the indicator to locate each version of each project.
		final List<S3ObjectSummary> summaries = s3BucketRepository.getObjectSummaries("")
				.stream()
				.filter(p -> p.getKey().contains(P2_INDEX_KEY_ELEM))
				.collect(Collectors.toList());

		// Sort the object summaries by their corresponding project name
		final Map<String, List<S3ObjectSummary>> summariesByProject = new HashMap<>();
		for (final S3ObjectSummary summary : summaries) {
			final String name = summary.getKey().split(KEY_DELIM)[0];
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
			Optional<S3ObjectSummary> mostRecentSummary = Optional.empty();
			Optional<ProjectVersion> mostRecentVersion = Optional.empty();
			for (final S3ObjectSummary summary : entry.getValue()) {
				final String[] objectKeyTokens = summary.getKey().split(KEY_DELIM);
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
				if (!mostRecentSummary.isPresent() || (version.compareTo(mostRecentVersion.get()) < 1)) {
					mostRecentSummary = Optional.of(summary);
					mostRecentVersion = Optional.of(version);
				}
			}

			if (!mostRecentSummary.isPresent()) {
				continue;
			}

			// Order the versions
			Collections.sort(snapshots);
			Collections.sort(releases);

			// Create the new project object, overriding the name from the metadata with the project name
			final String key = mostRecentSummary.get().getKey().replace(KEY_DELIM + P2_INDEX_KEY_ELEM, "");
			final P2Repository metadata = metadataRepository.getMetadata(s3BucketRepository.getHostingUrl(key));
			final P2Repository updatedMetadata = new P2Repository(name, metadata.getLocation(), metadata.isCompressed(),
					metadata.getLastModified(), metadata.getGroups());
			final String customDomain = properties.getCustomDomain();
			final String latestSnapshotUrl = String.format(properties.getLatestSnapshotContentUrlFormat(), customDomain, name);
			final String latestReleaseUrl = String.format(properties.getLatestReleaseContentUrlFormat(), customDomain, name);
			final String genericUrl = String.format(properties.getGenericContentUrlFormat(), customDomain, name);
			projects.put(name, new Project(updatedMetadata, snapshots, releases, mostRecentVersion.get(),
					latestSnapshotUrl, latestReleaseUrl, genericUrl));
		}

		return projects;
	}

}
