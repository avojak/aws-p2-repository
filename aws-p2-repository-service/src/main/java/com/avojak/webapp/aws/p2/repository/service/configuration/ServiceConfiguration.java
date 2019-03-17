package com.avojak.webapp.aws.p2.repository.service.configuration;

import com.google.common.base.Ticker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ServiceConfiguration {

	@Value("${aws.p2.repo.cache.expiration}")
	private long cacheExpirationDuration;

	@Value("${aws.p2.repo.cache.expiration.units}")
	private TimeUnit cacheExpirationUnits;

	@Value("${aws.p2.repo.latest.snapshot.content.url.format}")
	private String latestSnapshotContentUrlFormat;

	@Value("${aws.p2.repo.latest.release.content.url.format}")
	private String latestReleaseContentUrlFormat;

	@Value("${aws.p2.repo.generic.content.url.format}")
	private String genericContentUrlFormat;

	@Value("${aws.p2.repo.webapp.custom.domain}")
	private String customDomain;

	@Bean
	public Ticker ticker() {
		return Ticker.systemTicker();
	}

	@Bean
	public ServiceProperties serviceProperties() {
		return new ServiceProperties(cacheExpirationDuration, cacheExpirationUnits, latestSnapshotContentUrlFormat,
				latestReleaseContentUrlFormat, genericContentUrlFormat, customDomain);
	}

}
