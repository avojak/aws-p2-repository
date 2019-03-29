package com.avojak.webapp.aws.p2.repository.model.repository;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.net.URI;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models a license.
 */
public final class License {

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
		License rhs = (License) obj;
		return new EqualsBuilder()
				.append(name, rhs.name)
				.append(body, rhs.body)
				.append(location, rhs.location)
				.build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(name)
				.append(body)
				.append(location)
				.build();
	}

}
