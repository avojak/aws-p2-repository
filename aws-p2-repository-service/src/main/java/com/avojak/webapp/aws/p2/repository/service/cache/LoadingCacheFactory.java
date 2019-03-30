package com.avojak.webapp.aws.p2.repository.service.cache;

import com.avojak.webapp.aws.p2.repository.model.project.Project;
import com.avojak.webapp.aws.p2.repository.service.configuration.ServiceProperties;
import com.google.common.base.Ticker;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Factory class to create new instances of {@link LoadingCache}.
 */
@Component
public class LoadingCacheFactory {

	private final ProjectCacheLoader cacheLoader;
	private final Ticker ticker;
	private final ServiceProperties properties;

	/**
	 * Constructor.
	 *
	 * @param cacheLoader
	 * 		The {@link ProjectCacheLoader}. Cannot be null.
	 * @param ticker
	 * 		The {@link Ticker}. Cannot be null.
	 * @param properties
	 * 		The {@link ServiceProperties}. Cannot be null.
	 */
	@Autowired
	public LoadingCacheFactory(final ProjectCacheLoader cacheLoader, final Ticker ticker,
							   final ServiceProperties properties) {
		this.cacheLoader = checkNotNull(cacheLoader, "cacheLoader cannot be null");
		this.ticker = checkNotNull(ticker, "ticker cannot be null");
		this.properties = checkNotNull(properties, "properties cannot be null");
	}

	/**
	 * Creates a new instance of {@link LoadingCache}.
	 *
	 * @return The new, non-null {@link LoadingCache}.
	 */
	public LoadingCache<Boolean, Map<String, Project>> create() {
		return CacheBuilder.newBuilder()
				.expireAfterWrite(properties.getCacheExpirationDuration(), properties.getCacheExpirationUnits())
				.ticker(ticker)
				.build(cacheLoader);
	}

}
