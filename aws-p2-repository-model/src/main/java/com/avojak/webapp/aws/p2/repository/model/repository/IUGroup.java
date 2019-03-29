package com.avojak.webapp.aws.p2.repository.model.repository;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models an installable unit group.
 */
public final class IUGroup {

	private final String name;
	private final String id;
	private final String description;
	private final String copyright;
	private final Collection<License> licenses;
	private final String version;

	/**
	 * Constructor.
	 *
	 * @param name
	 * 		The group name. Cannot be null or empty.
	 * @param id
	 * 		The id. Cannot be null or empty.
	 * @param description
	 * 		The description. Cannot be null.
	 * @param copyright
	 * 		The copyright. Cannot be null.
	 * @param licenses
	 * 		The collection of licenses. Cannot be null.
	 * @param version
	 * 		The version. Cannot be null or empty.
	 */
	public IUGroup(final String name, final String id, final String description, final String copyright,
				   final Collection<License> licenses, final String version) {
		this.name = checkNotNull(name, "name cannot be null");
		checkArgument(!name.trim().isEmpty(), "name cannot be empty");
		this.id = checkNotNull(id, "id cannot be null");
		checkArgument(!id.trim().isEmpty(), "id cannot be empty");
		this.description = checkNotNull(description, "description cannot be null");
		this.copyright = checkNotNull(copyright, "copyright cannot be null");
		this.licenses = checkNotNull(licenses, "licenses cannot be null");
		this.version = checkNotNull(version, "version cannot be null");
		checkArgument(!version.trim().isEmpty(), "version cannot be empty");
	}

	/**
	 * Gets the group name.
	 *
	 * @return The non-null, non-empty group name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the group ID.
	 *
	 * @return The non-null, non-empty group ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the description.
	 *
	 * @return The non-null, possibly empty description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the copyright.
	 *
	 * @return The non-null, possibly empty copyright.
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * The collection of licenses.
	 *
	 * @return The non-null, possibly empty collection of {@link License} objects.
	 */
	public Collection<License> getLicenses() {
		return licenses;
	}

	/**
	 * Gets the version.
	 *
	 * @return The non-null, non-empty version.
	 */
	public String getVersion() {
		return version;
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
		IUGroup rhs = (IUGroup) obj;
		return new EqualsBuilder()
				.append(name, rhs.name)
				.append(id, rhs.id)
				.append(description, rhs.description)
				.append(copyright, rhs.copyright)
				.append(licenses, rhs.licenses)
				.append(version, rhs.version)
				.build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(name)
				.append(id)
				.append(description)
				.append(copyright)
				.append(licenses)
				.append(version)
				.build();
	}

}
