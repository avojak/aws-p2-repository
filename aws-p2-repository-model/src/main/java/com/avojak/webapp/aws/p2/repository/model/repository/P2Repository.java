package com.avojak.webapp.aws.p2.repository.model.repository;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.net.URI;
import java.util.Collection;

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
		P2Repository rhs = (P2Repository) obj;
		return new EqualsBuilder()
				.append(name, rhs.name)
				.append(location, rhs.location)
				.append(isCompressed, rhs.isCompressed)
				.append(lastModified, rhs.lastModified)
				.append(groups, rhs.groups)
				.build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(name)
				.append(location)
				.append(isCompressed)
				.append(lastModified)
				.append(groups)
				.build();
	}

}
