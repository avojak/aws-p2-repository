package com.avojak.webapp.aws.p2.repository.model.project;

import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models a project and all available versions.
 */
public class Project implements Comparable<Project> {

	private final P2Repository metadata;
	private final ProjectVersion latestVersion;
	private final List<ProjectVersion> snapshots;
	private final List<ProjectVersion> releases;
	private final String latestSnapshotUrl;
	private final String latestReleaseUrl;
	private final String genericUrl;

	/**
	 * Constructor.
	 *
	 * @param metadata
	 * 		The project metadata. Cannot be null.
	 * @param snapshots
	 * 		The list of snapshot versions. Cannot be null.
	 * @param releases
	 * 		The list of release versions. Cannot be null
	 * @param latestVersion
	 * 		The latest version. Cannot be null.
	 * @param latestSnapshotUrl
	 * @param latestReleaseUrl
	 * @param genericUrl
	 */
	public Project(final P2Repository metadata, final List<ProjectVersion> snapshots,
				   final List<ProjectVersion> releases, final ProjectVersion latestVersion,
				   final String latestSnapshotUrl, final String latestReleaseUrl, final String genericUrl) {
		this.metadata = checkNotNull(metadata, "metadata cannot be null");
		this.snapshots = checkNotNull(snapshots, "snapshots cannot be null");
		this.releases = checkNotNull(releases, "releases cannot be null");
		this.latestVersion = checkNotNull(latestVersion, "latestVersion cannot be null");
		this.latestSnapshotUrl = checkNotNull(latestSnapshotUrl, "latestSnapshotUrl cannot be null");
		checkArgument(!latestSnapshotUrl.trim().isEmpty(), "latestSnapshotUrl cannot be empty");
		this.latestReleaseUrl = checkNotNull(latestReleaseUrl, "latestReleaseUrl cannot be null");
		checkArgument(!latestReleaseUrl.trim().isEmpty(), "latestReleaseUrl cannot be empty");
		this.genericUrl = checkNotNull(genericUrl, "genericUrl cannot be null");
		checkArgument(!genericUrl.trim().isEmpty(), "genericUrl cannot be empty");
	}

	public P2Repository getMetadata() {
		return metadata;
	}

	public ProjectVersion getLatestVersion() {
		return latestVersion;
	}

	public List<ProjectVersion> getSnapshots() {
		return snapshots;
	}

	public List<ProjectVersion> getReleases() {
		return releases;
	}

	public String getLatestSnapshotUrl() {
		return latestSnapshotUrl;
	}

	public String getLatestReleaseUrl() {
		return latestReleaseUrl;
	}

	public String getGenericUrl() {
		return genericUrl;
	}

	@Override
	public int compareTo(final Project project) {
		checkNotNull(project, "project cannot be null");
		return metadata.getName().compareToIgnoreCase(project.getMetadata().getName());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass()) {
			return false;
		}
		Project rhs = (Project) obj;
		return new EqualsBuilder()
				.append(metadata, rhs.metadata)
				.append(latestVersion, rhs.latestVersion)
				.append(snapshots, rhs.snapshots)
				.append(releases, rhs.releases)
				.append(latestSnapshotUrl, rhs.latestSnapshotUrl)
				.append(latestReleaseUrl, rhs.latestReleaseUrl)
				.append(genericUrl, rhs.genericUrl)
				.build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(metadata)
				.append(latestVersion)
				.append(snapshots)
				.append(releases)
				.append(latestSnapshotUrl)
				.append(latestReleaseUrl)
				.append(genericUrl)
				.build();
	}

}
