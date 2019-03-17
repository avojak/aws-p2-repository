package com.avojak.webapp.aws.p2.repository.service.configuration;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ServiceProperties {

	private final long cacheExpirationDuration;
	private final TimeUnit cacheExpirationUnits;
	private final String latestSnapshotContentUrlFormat;
	private final String latestReleaseContentUrlFormat;
	private final String genericContentUrlFormat;
	private final String customDomain;

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
}
