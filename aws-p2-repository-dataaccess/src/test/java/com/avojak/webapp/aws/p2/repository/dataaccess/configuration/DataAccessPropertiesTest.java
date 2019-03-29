package com.avojak.webapp.aws.p2.repository.dataaccess.configuration;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link DataAccessProperties}.
 */
public class DataAccessPropertiesTest {

	private final String bucketName = "p2.avojak.com";
	private final int maxKeys = 100;
	private final List<String> excludes = Arrays.asList("index.html");
	private final String objectUrlFormat = "{0}";
	private final String metadataServiceBaseUrl = "https://www.example.com";

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullBucketName() {
		new DataAccessProperties(null, maxKeys, excludes, objectUrlFormat, metadataServiceBaseUrl);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyBucketName() {
		new DataAccessProperties(" ", maxKeys, excludes, objectUrlFormat, metadataServiceBaseUrl);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_NegativeMaxKeys() {
		new DataAccessProperties(bucketName, -1, excludes, objectUrlFormat, metadataServiceBaseUrl);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_ZeroMaxKeys() {
		new DataAccessProperties(bucketName, 0, excludes, objectUrlFormat, metadataServiceBaseUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullExcludes() {
		new DataAccessProperties(bucketName, maxKeys, null, objectUrlFormat, metadataServiceBaseUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullObjectUrlFormat() {
		new DataAccessProperties(bucketName, maxKeys, excludes, null, metadataServiceBaseUrl);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyObjectUrlFormat() {
		new DataAccessProperties(bucketName, maxKeys, excludes, " ", metadataServiceBaseUrl);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullMetadataServiceBaseUrl() {
		new DataAccessProperties(bucketName, maxKeys, excludes, objectUrlFormat, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testConstructor_EmptyMetadataServiceBaseUrl() {
		new DataAccessProperties(bucketName, maxKeys, excludes, objectUrlFormat, " ");
	}

	@Test
	public void testGetters() {
		final DataAccessProperties properties = new DataAccessProperties(bucketName, maxKeys, excludes, objectUrlFormat, metadataServiceBaseUrl);
		assertEquals(bucketName, properties.getBucketName());
		assertEquals(maxKeys, properties.getMaxKeys());
		assertEquals(excludes, properties.getExcludes());
		assertEquals(objectUrlFormat, properties.getObjectUrlFormat());
		assertEquals(metadataServiceBaseUrl, properties.getMetadataServiceBaseUrl());
	}

	@Test
	public void testEquals() {
		EqualsVerifier.forClass(DataAccessProperties.class).verify();
	}

}
