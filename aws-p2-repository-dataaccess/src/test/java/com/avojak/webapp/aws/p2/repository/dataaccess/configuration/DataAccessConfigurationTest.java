package com.avojak.webapp.aws.p2.repository.dataaccess.configuration;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link DataAccessConfiguration}.
 */
public class DataAccessConfigurationTest {

	private final String bucketName = "p2.avojak.com";
	private final int maxKeys = 100;
	private final String[] excludes = new String[]{ "index.html" };
	private final String objectUrlFormat = "http://s3.amazonaws.com/{0}/{1}";
	private final String metadataServiceBaseUrl = "https://www.example.com";

	private DataAccessConfiguration configuration;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		configuration = new DataAccessConfiguration();
		ReflectionTestUtils.setField(configuration, "bucketName", bucketName);
		ReflectionTestUtils.setField(configuration, "maxKeys", maxKeys);
		ReflectionTestUtils.setField(configuration, "excludes", excludes);
		ReflectionTestUtils.setField(configuration, "objectUrlFormat", objectUrlFormat);
		ReflectionTestUtils.setField(configuration, "metadataServiceBaseUrl", metadataServiceBaseUrl);
	}

	/**
	 * Tests that {@link DataAccessConfiguration#amazonS3()} creates a new, non-null {@link
	 * com.amazonaws.services.s3.AmazonS3} instance.
	 */
	@Test
	public void testAmazonS3() {
		assertNotNull(configuration.amazonS3());
	}

	/**
	 * Tests that {@link DataAccessConfiguration#dataAccessProperties()} creates a new, non-null {@link
	 * DataAccessProperties} instance.
	 */
	@Test
	public void testDataAccessProperties() {
		assertNotNull(configuration.dataAccessProperties());
	}

	/**
	 * Tests that {@link DataAccessConfiguration#httpClient()} creates a new, non-null {@link
	 * org.apache.http.client.HttpClient} instance.
	 */
	@Test
	public void testHttpClient() {
		assertNotNull(configuration.httpClient());
	}

	/**
	 * Tests that {@link DataAccessConfiguration#gson()} creates a new, non-null {@link com.google.gson.Gson} instance.
	 */
	@Test
	public void testGson() {
		assertNotNull(configuration.gson());
	}

}
