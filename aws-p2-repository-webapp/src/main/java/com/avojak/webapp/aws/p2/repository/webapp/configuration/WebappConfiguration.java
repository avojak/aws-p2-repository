package com.avojak.webapp.aws.p2.repository.webapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Web application configuration.
 */
@Configuration
public class WebappConfiguration {

	@Value("${aws.p2.repo.webapp.brand.name}")
	private String brandName;

	@Value("${aws.p2.repo.webapp.brand.icon}")
	private String brandIcon;

	@Value("${aws.p2.repo.webapp.brand.favicon}")
	private String brandFavicon;

	@Value("${aws.p2.repo.webapp.custom.domain}")
	private String customDomain;

	@Value("${aws.p2.repo.webapp.welcome.message}")
	private String welcomeMessage;

	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	/**
	 * Creates the {@link WebappProperties}.
	 *
	 * @return The non-null {@link WebappProperties}.
	 */
	@Bean
	public WebappProperties webappProperties() {
		return new WebappProperties(brandName, brandIcon, brandFavicon, customDomain, welcomeMessage, bucketName);
	}

}
