package com.avojak.webapp.aws.p2.repository.model.project;

import org.apache.maven.artifact.versioning.ComparableVersion;

import java.util.Date;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models a version of a project.
 */
public class ProjectVersion implements Comparable<ProjectVersion> {

	private final ComparableVersion version;
	private final Date date;

	/**
	 * Constructor.
	 *
	 * @param version
	 * 		The {@link ComparableVersion}. Cannot be null.
	 * @param date
	 * 		The date that the version was last modified. Cannot be null.
	 */
	public ProjectVersion(final ComparableVersion version, final Date date) {
		this.version = checkNotNull(version, "version cannot be null");
		this.date = checkNotNull(date, "date cannot be null");
	}

	@Override
	public int compareTo(final ProjectVersion other) {
		checkNotNull(other, "other cannot be null");
		return version.compareTo(other.getVersion());
	}


	/**
	 * Gets the version.
	 *
	 * @return The non-null {@link ComparableVersion}.
	 */
	public ComparableVersion getVersion() {
		return version;
	}

	/**
	 * Gets the date.
	 *
	 * @return The non-null {@link Date}.
	 */
	public Date getDate() {
		return date;
	}

}
