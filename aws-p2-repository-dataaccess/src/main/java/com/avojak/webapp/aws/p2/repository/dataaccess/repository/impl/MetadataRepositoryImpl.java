package com.avojak.webapp.aws.p2.repository.dataaccess.repository.impl;

import com.avojak.webapp.aws.p2.repository.dataaccess.repository.MetadataRepository;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.configuration.DataAccessProperties;
import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;
import com.google.gson.Gson;
import org.apache.commons.codec.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
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

@Component
public class MetadataRepositoryImpl implements MetadataRepository {

	private static final Logger LOGGER = LoggerFactory.getLogger(MetadataRepositoryImpl.class);
	private static final String METADATA_ENDPOINT = "/repository";

	private final HttpClient httpClient;
	private final Gson gson;
	private final DataAccessProperties properties;

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
					.setParameter("url", url)
					.build();
		} catch (final URISyntaxException e) {
			throw new IllegalArgumentException(e);
		}
		LOGGER.debug(uri.toString());
		final HttpGet request = new HttpGet(uri);
		// TODO: set content type for JSON
		final HttpResponse response;
		try {
			response = httpClient.execute(request);
		} catch (IOException e) {
			LOGGER.error("Failed to execute GET for url: " + url, e);
			throw new RuntimeException(e);
		}
		try {
			final String json = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
			LOGGER.debug(json);
			return gson.fromJson(json, P2Repository.class);
		} catch (IOException e) {
			LOGGER.error("Failed to retrieve entity from response", e);
			throw new RuntimeException(e);
		}
	}

}
