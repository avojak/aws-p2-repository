package com.avojak.webapp.aws.p2.repository.model.repository;

import java.util.Collection;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models an installable unit group.
 */
public class IUGroup {

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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		IUGroup iuGroup = (IUGroup) o;
		return Objects.equals(name, iuGroup.name) &&
				Objects.equals(id, iuGroup.id) &&
				Objects.equals(description, iuGroup.description) &&
				Objects.equals(copyright, iuGroup.copyright) &&
				Objects.equals(licenses, iuGroup.licenses) &&
				Objects.equals(version, iuGroup.version);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, id, description, copyright, licenses, version);
	}
}
