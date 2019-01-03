package com.avojak.webapp.aws.p2.repository.dataaccess.repository.configuration;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Data access properties.
 */
public class DataAccessProperties {

	private final String bucketName;
	private final int maxKeys;
	private final List<String> excludes;

	/**
	 * Constructor.
	 *
	 * @param bucketName
	 * 		The bucket name. Cannot be null or empty.
	 * @param maxKeys
	 * 		The maximum number of keys per request. Must be a positive number.
	 * @param excludes
	 * 		The list of object keys to exclude from results.
	 */
	DataAccessProperties(final String bucketName, final int maxKeys, final List<String> excludes) {
		this.bucketName = checkNotNull(bucketName, "bucketName cannot be null");
		checkArgument(!bucketName.trim().isEmpty(), "bucketName cannot be empty");
		this.maxKeys = maxKeys;
		checkArgument(maxKeys > 0, "maxKeys must be a positive number");
		this.excludes = checkNotNull(excludes, "excludes cannot be null");
	}

	public String getBucketName() {
		return bucketName;
	}

	public int getMaxKeys() {
		return maxKeys;
	}

	public List<String> getExcludes() {
		return excludes;
	}
}
