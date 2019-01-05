package com.avojak.webapp.aws.p2.repository.service.configuration;

import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class ServiceProperties {

	private final long cacheExpirationDuration;
	private final TimeUnit cacheExpirationUnits;

	public ServiceProperties(final long cacheExpirationDuration, final TimeUnit cacheExpirationUnits) {
		this.cacheExpirationDuration = cacheExpirationDuration;
		this.cacheExpirationUnits = cacheExpirationUnits;
	}

	public long getCacheExpirationDuration() {
		return cacheExpirationDuration;
	}

	public TimeUnit getCacheExpirationUnits() {
		return cacheExpirationUnits;
	}

}
