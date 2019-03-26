package com.avojak.webapp.aws.p2.repository.dataaccess.repository.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.avojak.webapp.aws.p2.repository.dataaccess.configuration.DataAccessProperties;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.S3BucketRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link S3BucketRepositoryImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class S3BucketRepositoryImplTest {

	@Mock
	private AmazonS3 client;

	@Mock
	private DataAccessProperties properties;

	private S3BucketRepository repository;

	@Before
	public void setup() {
		when(properties.getBucketName()).thenReturn("p2.avojak.com");
		when(properties.getMaxKeys()).thenReturn(100);
		when(properties.getExcludes()).thenReturn(new ArrayList<>());
		when(properties.getObjectUrlFormat()).thenReturn("http://s3.amazonaws.com/{0}/{1}");

		repository = new S3BucketRepositoryImpl(client, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullClient() {
		new S3BucketRepositoryImpl(null, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullProperties() {
		new S3BucketRepositoryImpl(client, null);
	}

	@Test(expected = NullPointerException.class)
	public void testGetObject_NullKey() {
		repository.getObject(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetObject_EmptyKey() {
		repository.getObject(" ");
	}

	@Test
	public void testGetObject_ExcludedKey() {
		when(properties.getExcludes()).thenReturn(Arrays.asList("mock"));
		assertFalse(repository.getObject("mock").isPresent());
	}

	@Test
	public void testGetObject_ObjectDoesNotExist() {
		when(client.doesObjectExist("p2.avojak.com", "mock")).thenReturn(false);
		assertFalse(repository.getObject("mock").isPresent());
	}

	@Test
	public void testGetObject() {
		when(client.doesObjectExist("p2.avojak.com", "mock")).thenReturn(true);
		final ArgumentCaptor<GetObjectRequest> requestCaptor = ArgumentCaptor.forClass(GetObjectRequest.class);
		final S3Object expectedObject = mock(S3Object.class);
		when(client.getObject(requestCaptor.capture())).thenReturn(expectedObject);

		final Optional<S3Object> object = repository.getObject("mock");

		final GetObjectRequest request = requestCaptor.getValue();
		assertEquals("p2.avojak.com", request.getBucketName());
		assertEquals("mock", request.getKey());
		assertTrue(object.isPresent());
		assertEquals(expectedObject, object.get());
	}

	@Test(expected = NullPointerException.class)
	public void testGetObjectSummaries_NullPrefix() {
		repository.getObjectSummaries(null);
	}

	@Test
	public void testGetObjectSummaries() {
		final ArgumentCaptor<ListObjectsV2Request> requestCaptor = ArgumentCaptor.forClass(ListObjectsV2Request.class);
		when(properties.getExcludes()).thenReturn(Arrays.asList("excluded"));
		final ListObjectsV2Result result = mock(ListObjectsV2Result.class);
		final S3ObjectSummary summary1 = mock(S3ObjectSummary.class);
		when(summary1.getKey()).thenReturn("excluded");
		final S3ObjectSummary summary2 = mock(S3ObjectSummary.class);
		when(summary2.getKey()).thenReturn("mock");

		when(result.isTruncated()).thenReturn(true, false);
		when(result.getObjectSummaries()).thenReturn(Arrays.asList(summary1), Arrays.asList(summary2));
		when(client.listObjectsV2(requestCaptor.capture())).thenReturn(result);

		final List<S3ObjectSummary> summaries = repository.getObjectSummaries("prefix");

		assertEquals(1, summaries.size());
		assertEquals(summary2, summaries.get(0));
	}

	@Test(expected = NullPointerException.class)
	public void testGetHostingURL_NullKey() {
		repository.getHostingUrl(null);
	}

	@Test
	public void testGetHostingURL() {
		final String url = repository.getHostingUrl("mock");
		assertEquals("http://s3.amazonaws.com/p2.avojak.com/mock", url);
	}

}
