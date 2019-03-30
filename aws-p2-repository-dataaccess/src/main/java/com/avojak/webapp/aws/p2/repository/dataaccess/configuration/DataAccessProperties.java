package com.avojak.webapp.aws.p2.repository.dataaccess.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Data-access properties.
 */
public final class DataAccessProperties {

	private final String bucketName;
	private final int maxKeys;
	private final List<String> excludes;
	private final String objectUrlFormat;
	private final String metadataServiceBaseUrl;

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
	public DataAccessProperties(final String bucketName, final int maxKeys, final List<String> excludes,
								final String objectUrlFormat, final String metadataServiceBaseUrl) {
		this.bucketName = checkNotNull(bucketName, "bucketName cannot be null");
		checkArgument(!bucketName.trim().isEmpty(), "bucketName cannot be empty");
		this.maxKeys = maxKeys;
		checkArgument(maxKeys > 0, "maxKeys must be a positive number");
		this.excludes = checkNotNull(excludes, "excludes cannot be null");
		this.objectUrlFormat = checkNotNull(objectUrlFormat, "objectUrlFormat cannot be null");
		checkArgument(!objectUrlFormat.trim().isEmpty(), "objectUrlFormat cannot be empty");
		this.metadataServiceBaseUrl = checkNotNull(metadataServiceBaseUrl, "metadataServiceBaseUrl cannot be null");
		checkArgument(!metadataServiceBaseUrl.trim().isEmpty(), "metadataServiceBaseUrl cannot be empty");
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

	public String getObjectUrlFormat() {
		return objectUrlFormat;
	}

	public String getMetadataServiceBaseUrl() {
		return metadataServiceBaseUrl;
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
		DataAccessProperties rhs = (DataAccessProperties) obj;
		return new EqualsBuilder()
				.append(bucketName, rhs.bucketName)
				.append(maxKeys, rhs.maxKeys)
				.append(excludes, rhs.excludes)
				.append(objectUrlFormat, rhs.objectUrlFormat)
				.append(metadataServiceBaseUrl, rhs.metadataServiceBaseUrl)
				.build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(bucketName)
				.append(maxKeys)
				.append(excludes)
				.append(objectUrlFormat)
				.append(metadataServiceBaseUrl)
				.build();
	}
}
