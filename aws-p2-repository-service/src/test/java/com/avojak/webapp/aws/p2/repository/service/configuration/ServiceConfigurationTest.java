package com.avojak.webapp.aws.p2.repository.service.configuration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link ServiceConfiguration}.
 */
public class ServiceConfigurationTest {

	private ServiceConfiguration configuration;

	@Before
	public void setup() {
		configuration = new ServiceConfiguration();
		ReflectionTestUtils.setField(configuration, "cacheExpirationDuration", 4L);
		ReflectionTestUtils.setField(configuration, "cacheExpirationUnits", TimeUnit.HOURS);
		ReflectionTestUtils.setField(configuration, "latestSnapshotContentUrlFormat", "https://%s/content/%s/releases/latest");
		ReflectionTestUtils.setField(configuration, "latestReleaseContentUrlFormat", "https://%s/content/%s/snapshots/latest");
		ReflectionTestUtils.setField(configuration, "genericContentUrlFormat", "https://%s/content/%s/{snapshots|releases}/{version}");
		ReflectionTestUtils.setField(configuration, "customDomain", "p2.avojak.com");
	}

	@Test
	public void testTicker() {
		assertNotNull(configuration.ticker());
	}

	@Test
	public void testServiceProperties() {
		assertNotNull(configuration.serviceProperties());
	}

}
