package com.avojak.webapp.aws.p2.repository.dataaccess.repository.impl;

import com.amazonaws.http.HttpMethodName;
import com.avojak.webapp.aws.p2.repository.dataaccess.configuration.DataAccessProperties;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.MetadataRepository;
import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link MetadataRepositoryImpl}.
 */
@RunWith(MockitoJUnitRunner.class)
public class MetadataRepositoryImplTest {

	@Mock
	private HttpClient httpClient;

	@Mock
	private DataAccessProperties properties;

	private final String url = "https://p2.avojak.com";

	private Gson gson;
	private MetadataRepository repository;

	@Before
	public void setup() {
		gson = new GsonBuilder().setPrettyPrinting().create();
		when(properties.getMetadataServiceBaseUrl()).thenReturn("https://www.example.com");
		repository = new MetadataRepositoryImpl(httpClient, gson, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullHttpClient() {
		new MetadataRepositoryImpl(null, gson, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullGson() {
		new MetadataRepositoryImpl(httpClient, null, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullProperties() {
		new MetadataRepositoryImpl(httpClient, gson, null);
	}

	@Test(expected = NullPointerException.class)
	public void testGetMetadata_NullUrl() {
		repository.getMetadata(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetMetadata_EmptyUrl() {
		repository.getMetadata(" ");
	}

	@Test(expected = RuntimeException.class)
	public void testGetMetadata_FailedToRetrieveMetadata() throws IOException {
		when(httpClient.execute(Mockito.isA(HttpGet.class))).thenThrow(IOException.class);
		repository.getMetadata(url);
	}

	@Test
	public void testGetMetadata() throws URISyntaxException, IOException {
		final ArgumentCaptor<HttpGet> requestCaptor = ArgumentCaptor.forClass(HttpGet.class);
		final P2Repository expectedRepository = new P2Repository("Name", new URI("https://p2.avojak.com"), true,
				System.currentTimeMillis(), new HashSet<>());
		final HttpResponse httpResponse = mock(HttpResponse.class);
		when(httpClient.execute(Mockito.isA(HttpGet.class))).thenReturn(httpResponse);
		final HttpEntity httpEntity = new StringEntity(gson.toJson(expectedRepository));
		when(httpResponse.getEntity()).thenReturn(httpEntity);

		final P2Repository actualRepository = repository.getMetadata(url);

		verify(httpClient).execute(requestCaptor.capture());
		assertEquals(HttpMethodName.GET.name(), requestCaptor.getValue().getMethod());
		assertEquals("https://www.example.com/repository?url=https%3A%2F%2Fp2.avojak.com",
				requestCaptor.getValue().getURI().toString());
		assertEquals("application/json", requestCaptor.getValue().getFirstHeader(HttpHeaders.ACCEPT).getValue());

		assertEquals(expectedRepository, actualRepository);
	}

}
