package com.avojak.webapp.aws.p2.repository.model.repository;

import java.net.URI;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models a license.
 */
public class License {

	private final String name;
	private final String body;
	private final URI location;

	/**
	 * Constructor.
	 *
	 * @param name
	 * 		The name. Cannot be null or empty.
	 * @param body
	 * 		The body. Cannot be null or empty.
	 * @param location
	 * 		The location.
	 */
	public License(final String name, final String body, final URI location) {
		this.name = checkNotNull(name, "name cannot be null");
		checkArgument(!name.trim().isEmpty(), "name cannot be empty");
		this.body = checkNotNull(body, "body cannot be null");
		checkArgument(!body.trim().isEmpty(), "body cannot be empty");
		this.location = location;
	}

	/**
	 * Gets the name of the license.
	 *
	 * @return The non-null, non-empty name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the body of the license.
	 *
	 * @return The non-null, non-empty body.
	 */
	public String getBody() {
		return body;
	}

	/**
	 * Gets the location of the license.
	 *
	 * @return The location {@link URI}.
	 */
	public URI getLocation() {
		return location;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		License license = (License) o;
		return Objects.equals(name, license.name) &&
				Objects.equals(body, license.body) &&
				Objects.equals(location, license.location);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, body, location);
	}
}
