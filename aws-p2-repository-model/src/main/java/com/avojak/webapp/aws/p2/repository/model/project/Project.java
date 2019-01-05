package com.avojak.webapp.aws.p2.repository.model.project;

import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models a project and all available versions.
 */
public class Project implements Comparable<Project> {

	private final String name;
	private final ProjectVersion latestVersion;
	private final List<ProjectVersion> snapshots;
	private final List<ProjectVersion> releases;

	/**
	 * Constructor.
	 *
	 * @param name
	 * 		The project name. Cannot be null or empty.
	 * @param snapshots
	 * 		The list of snapshot versions. Cannot be null.
	 * @param releases
	 * 		The list of release versions. Cannot be null.
	 */
	public Project(final String name, final List<ProjectVersion> snapshots, final List<ProjectVersion> releases) {
		this.name = checkNotNull(name, "name cannot be null");
		checkArgument(!name.trim().isEmpty(), "name cannot be empty");
		this.snapshots = checkNotNull(snapshots, "snapshots cannot be null");
		this.releases = checkNotNull(releases, "releases cannot be null");
		Collections.sort(snapshots);
		Collections.sort(releases);
		this.latestVersion = determineLatestVersion(snapshots, releases);
	}

	private ProjectVersion determineLatestVersion(final List<ProjectVersion> snapshots,
												  final List<ProjectVersion> releases) {
		if (snapshots.isEmpty() && releases.isEmpty()) {
			return null;
		} else if (snapshots.isEmpty()) {
			return releases.get(0);
		} else if (releases.isEmpty()) {
			return snapshots.get(0);
		} else {
			final ProjectVersion latestSnapshot = snapshots.get(0);
			final ProjectVersion latestRelease = releases.get(0);
			if (latestSnapshot.getVersion().compareTo(latestRelease.getVersion()) < 0) {
				return latestSnapshot;
			} else {
				return latestRelease;
			}
		}
	}

	public String getName() {
		return name;
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

	@Override
	public int compareTo(final Project project) {
		checkNotNull(project, "project cannot be null");
		return name.compareToIgnoreCase(project.name);
	}
}
