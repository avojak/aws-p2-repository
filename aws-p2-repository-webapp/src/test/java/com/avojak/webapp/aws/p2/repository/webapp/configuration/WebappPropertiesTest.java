package com.avojak.webapp.aws.p2.repository.webapp.configuration;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link WebappProperties}.
 */
public class WebappPropertiesTest {

	@Test
	public void testGetters() {
		final WebappProperties properties = new WebappProperties("brandName", "brandIcon", "brandFavicon",
				"customDomain", "welcomeMessage", "bucketName");
		assertEquals("brandName", properties.getBrandName());
		assertEquals("brandIcon", properties.getBrandIcon());
		assertEquals("brandFavicon", properties.getBrandFavicon());
		assertEquals("customDomain", properties.getCustomDomain());
		assertEquals("welcomeMessage", properties.getWelcomeMessage());
		assertEquals("bucketName", properties.getBucketName());
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(WebappProperties.class).verify();
	}

}
