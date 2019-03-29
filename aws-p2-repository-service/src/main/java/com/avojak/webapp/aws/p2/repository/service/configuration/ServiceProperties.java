package com.avojak.webapp.aws.p2.repository.service.configuration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Models the service properties.
 */
@Component
public final class ServiceProperties {

	private final long cacheExpirationDuration;
	private final TimeUnit cacheExpirationUnits;
	private final String latestSnapshotContentUrlFormat;
	private final String latestReleaseContentUrlFormat;
	private final String genericContentUrlFormat;
	private final String customDomain;

	/**
	 * Constructor.
	 *
	 * @param cacheExpirationDuration
	 * 		The cache expiration duration.
	 * @param cacheExpirationUnits
	 * 		The units of the cache expiration duration.
	 * @param latestSnapshotContentUrlFormat
	 * 		The format of the URL for the latest snapshot content.
	 * @param latestReleaseContentUrlFormat
	 * 		The format of the URL for the latest release content.
	 * @param genericContentUrlFormat
	 * 		The format of the URL for generic version content.
	 * @param customDomain
	 * 		The custom domain name.
	 */
	public ServiceProperties(final long cacheExpirationDuration, final TimeUnit cacheExpirationUnits,
							 final String latestSnapshotContentUrlFormat, final String latestReleaseContentUrlFormat,
							 final String genericContentUrlFormat, final String customDomain) {
		this.cacheExpirationDuration = cacheExpirationDuration;
		this.cacheExpirationUnits = cacheExpirationUnits;
		this.latestSnapshotContentUrlFormat = latestSnapshotContentUrlFormat;
		this.latestReleaseContentUrlFormat = latestReleaseContentUrlFormat;
		this.genericContentUrlFormat = genericContentUrlFormat;
		this.customDomain = customDomain;
	}

	public long getCacheExpirationDuration() {
		return cacheExpirationDuration;
	}

	public TimeUnit getCacheExpirationUnits() {
		return cacheExpirationUnits;
	}

	public String getLatestSnapshotContentUrlFormat() {
		return latestSnapshotContentUrlFormat;
	}

	public String getLatestReleaseContentUrlFormat() {
		return latestReleaseContentUrlFormat;
	}

	public String getGenericContentUrlFormat() {
		return genericContentUrlFormat;
	}

	public String getCustomDomain() {
		return customDomain;
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
		ServiceProperties rhs = (ServiceProperties) obj;
		return new EqualsBuilder()
				.append(cacheExpirationDuration, rhs.cacheExpirationDuration)
				.append(cacheExpirationUnits, rhs.cacheExpirationUnits)
				.append(latestSnapshotContentUrlFormat, rhs.latestSnapshotContentUrlFormat)
				.append(latestReleaseContentUrlFormat, rhs.latestReleaseContentUrlFormat)
				.append(genericContentUrlFormat, rhs.genericContentUrlFormat)
				.append(customDomain, rhs.customDomain)
				.build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(cacheExpirationDuration)
				.append(cacheExpirationUnits)
				.append(latestSnapshotContentUrlFormat)
				.append(latestReleaseContentUrlFormat)
				.append(genericContentUrlFormat)
				.append(customDomain)
				.build();
	}
}
