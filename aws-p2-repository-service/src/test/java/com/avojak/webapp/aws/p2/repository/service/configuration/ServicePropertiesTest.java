package com.avojak.webapp.aws.p2.repository.service.configuration;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link ServiceProperties}.
 */
public class ServicePropertiesTest {

	@Test
	public void testGetters() {
		final ServiceProperties properties = new ServiceProperties(4L, TimeUnit.HOURS,
				"https://%s/content/%s/snapshots/latest", "https://%s/content/%s/releases/latest",
				"https://%s/content/%s/{snapshots|releases}/{version}", "p2.avojak.com");
		assertEquals(4L, properties.getCacheExpirationDuration());
		assertEquals(TimeUnit.HOURS, properties.getCacheExpirationUnits());
		assertEquals("https://%s/content/%s/snapshots/latest", properties.getLatestSnapshotContentUrlFormat());
		assertEquals("https://%s/content/%s/releases/latest", properties.getLatestReleaseContentUrlFormat());
		assertEquals("https://%s/content/%s/{snapshots|releases}/{version}", properties.getGenericContentUrlFormat());
		assertEquals("p2.avojak.com", properties.getCustomDomain());
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(ServiceProperties.class).verify();
	}

}
