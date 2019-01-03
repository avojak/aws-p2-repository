package com.avojak.webapp.aws.p2.repository.model;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Models the qualifiers for versions in the repository.
 */
public enum Qualifier {

	/**
	 * The SNAPSHOT qualifier.
	 */
	SNAPSHOT("snapshots"),

	/**
	 * The RELEASE qualifier.
	 */
	RELEASE("releases");

	private final String pathElement;

	Qualifier(final String pathElement) {
		this.pathElement = pathElement;
	}

	/**
	 * Retrieves the {@link Qualifier} for the given path element.
	 *
	 * @param pathElement
	 * 		The path element. Cannot be null or empty.
	 *
	 * @return The non-null {@link Qualifier}.
	 */
	public static Qualifier fromPathElement(final String pathElement) {
		checkNotNull(pathElement, "pathElement cannot be null");
		checkArgument(!pathElement.trim().isEmpty(), "pathElement cannot be empty");
		for (final Qualifier qualifier : Qualifier.values()) {
			if (qualifier.getPathElement().equals(pathElement)) {
				return qualifier;
			}
		}
		throw new IllegalArgumentException("No Qualifier found for path element: " + pathElement);
	}

	/**
	 * Gets the path element for the qualifier.
	 *
	 * @return The non-null, non-empty path element String.
	 */
	public String getPathElement() {
		return pathElement;
	}

}
