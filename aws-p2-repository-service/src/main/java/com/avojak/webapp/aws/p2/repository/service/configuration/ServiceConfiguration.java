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

	@Bean
	public Ticker ticker() {
		return Ticker.systemTicker();
	}

	@Bean
	public ServiceProperties serviceProperties() {
		return new ServiceProperties(cacheExpirationDuration, cacheExpirationUnits);
	}

}
