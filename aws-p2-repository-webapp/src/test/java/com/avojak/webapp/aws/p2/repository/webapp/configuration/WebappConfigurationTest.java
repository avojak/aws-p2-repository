package com.avojak.webapp.aws.p2.repository.webapp.configuration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link WebappConfiguration}.
 */
public class WebappConfigurationTest {

	private WebappConfiguration configuration;

	@Before
	public void setup() {
		configuration = new WebappConfiguration();
		ReflectionTestUtils.setField(configuration, "brandName", "Mock Brand");
		ReflectionTestUtils.setField(configuration, "brandIcon", "mock.icon");
		ReflectionTestUtils.setField(configuration, "brandFavicon", "mock.favicon");
		ReflectionTestUtils.setField(configuration, "customDomain", "mock.domain");
		ReflectionTestUtils.setField(configuration, "welcomeMessage", "Mock welcome");
		ReflectionTestUtils.setField(configuration, "bucketName", "mock-bucket");
	}

	@Test
	public void testWebappProperties() {
		assertNotNull(configuration.webappProperties());
	}

}
