package com.avojak.webapp.aws.p2.repository.dataaccess.repository.impl;

import com.avojak.webapp.aws.p2.repository.dataaccess.repository.MetadataRepository;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.configuration.DataAccessProperties;
import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import com.google.gson.Gson;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link MetadataRepository}.
 */
@Component
public class MetadataRepositoryImpl implements MetadataRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetadataRepositoryImpl.class);
	private static final String METADATA_ENDPOINT = "/repository";
	private static final String URL_PARAM = "url";

	private final HttpClient httpClient;
	private final Gson gson;
	private final DataAccessProperties properties;

	/**
	 * Constructor.
	 *
	 * @param httpClient
	 * 		The {@link HttpClient}. Cannot be null.
	 * @param gson
	 * 		The {@link Gson}. Cannot be null.
	 * @param properties
	 * 		The {@link DataAccessProperties}. Cannot be null.
	 */
	@Autowired
	public MetadataRepositoryImpl(final HttpClient httpClient, final Gson gson, final DataAccessProperties properties) {
		this.httpClient = checkNotNull(httpClient, "httpClient cannot be null");
		this.gson = checkNotNull(gson, "gson cannot be null");
		this.properties = checkNotNull(properties, "properties cannot be null");
	}

	@Override
	public P2Repository getMetadata(final String url) {
		checkNotNull(url, "url cannot be null");
		checkArgument(!url.trim().isEmpty(), "url cannot be empty");

		final URI uri;
		try {
			uri = new URIBuilder(properties.getMetadataServiceBaseUrl())
					.setPath(METADATA_ENDPOINT)
					.setParameter(URL_PARAM, url)
					.build();
		} catch (final URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		LOGGER.debug(uri.toString());
		final HttpGet request = new HttpGet(uri);
		request.setHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.getMimeType());
		try {
			final HttpResponse response = httpClient.execute(request);
			return gson.fromJson(EntityUtils.toString(response.getEntity(), Charsets.UTF_8), P2Repository.class);
		} catch (IOException e) {
			LOGGER.error("Failed to retrieve metadata", e);
			throw new RuntimeException(e);
		}
	}

}
