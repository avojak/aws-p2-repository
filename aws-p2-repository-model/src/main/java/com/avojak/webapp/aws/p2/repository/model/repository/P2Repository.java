package com.avojak.webapp.aws.p2.repository.model.repository;

import java.net.URI;
import java.util.Collection;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models the P2 repository details.
 */
public class P2Repository {

	private final String name;
	private final URI location;
	private final boolean isCompressed;
	private final long lastModified;
	private final Collection<IUGroup> groups;

	/**
	 * Constructor.
	 *
	 * @param name
	 * 		The repository name. Cannot be null or empty.
	 * @param location
	 * 		The repository location. Cannot be null.
	 * @param isCompressed
	 * 		Whether or not the repository is compressed.
	 * @param lastModified
	 * 		The timestamp of when the repository was last modified.
	 * @param groups
	 * 		The collection of installable unit groups. Cannot be null.
	 */
	public P2Repository(final String name, final URI location, final boolean isCompressed, final long lastModified,
						final Collection<IUGroup> groups) {
		this.name = checkNotNull(name, "name cannot be null");
		checkArgument(!name.trim().isEmpty(), "name cannot be empty");
		this.location = checkNotNull(location, "location cannot be null");
		this.isCompressed = isCompressed;
		this.lastModified = lastModified;
		this.groups = checkNotNull(groups, "groups cannot be null");
	}

	/**
	 * Gets the repository name.
	 *
	 * @return The non-null, non-empty name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the repository location.
	 *
	 * @return The non-null {@link URI}.
	 */
	public URI getLocation() {
		return location;
	}

	/**
	 * Returns whether or not the repository is compressed.
	 *
	 * @return {@code true} if the repository is compressed, otherwise {@code false}.
	 */
	public boolean isCompressed() {
		return isCompressed;
	}

	/**
	 * Gets the timestamp of when the repository was last modified.
	 *
	 * @return The timestamp.
	 */
	public long getLastModified() {
		return lastModified;
	}

	/**
	 * Gets the collection of installable unit groups.
	 *
	 * @return The non-null, possibly empty {@link Collection}.
	 */
	public Collection<IUGroup> getGroups() {
		return groups;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		P2Repository that = (P2Repository) o;
		return isCompressed == that.isCompressed &&
				lastModified == that.lastModified &&
				Objects.equals(name, that.name) &&
				Objects.equals(location, that.location) &&
				Objects.equals(groups, that.groups);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, location, isCompressed, lastModified, groups);
	}
}
