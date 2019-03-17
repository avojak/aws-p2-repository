package com.avojak.webapp.aws.p2.repository.dataaccess.repository.configuration;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Data-access configuration.
 */
@Configuration
public class DataAccessConfiguration {

	@Value("${aws.s3.bucket.name}")
	private String bucketName;

	@Value("${aws.s3.max.keys}")
	private int maxKeys;

	@Value("${aws.s3.excludes}")
	private String[] excludes;

	@Value("${aws.s3.object.url.format}")
	private String objectUrlFormat;

	@Value("${aws.p2.repo.inspector.base.url}")
	private String metadataServiceBaseUrl;

	/**
	 * Creates the {@link AmazonS3} client.
	 *
	 * @return The non-null {@link AmazonS3}.
	 */
	@Bean
	public AmazonS3 amazonS3() {
		return AmazonS3ClientBuilder.standard().withRegion(Regions.DEFAULT_REGION)
				.withForceGlobalBucketAccessEnabled(true)
				.withCredentials(new DefaultAWSCredentialsProviderChain())
				.build();
	}

	/**
	 * Creates the {@link DataAccessProperties}.
	 *
	 * @return The non-null {@link DataAccessProperties}.
	 */
	@Bean
	public DataAccessProperties dataAccessProperties() {
		return new DataAccessProperties(bucketName, maxKeys, Arrays.asList(excludes), objectUrlFormat, metadataServiceBaseUrl);
	}

	@Bean
	public HttpClient httpClient() {
		return HttpClientBuilder.create().build();
	}

	@Bean
	public Gson gson() {
		return new GsonBuilder().setPrettyPrinting().create();
	}

}
