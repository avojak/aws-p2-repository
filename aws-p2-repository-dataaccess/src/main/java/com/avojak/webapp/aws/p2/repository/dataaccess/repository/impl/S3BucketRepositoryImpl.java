package com.avojak.webapp.aws.p2.repository.dataaccess.repository.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.S3BucketRepository;
import com.avojak.webapp.aws.p2.repository.dataaccess.repository.configuration.DataAccessProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Implementation of {@link S3BucketRepository}.
 */
@Component
public class S3BucketRepositoryImpl implements S3BucketRepository {

	private final AmazonS3 client;
	private final DataAccessProperties properties;

	private String bucketRegion;

	/**
	 * Constructor.
	 *
	 * @param client
	 * 		The {@link AmazonS3} client. Cannot be null.
	 */
	@Autowired
	public S3BucketRepositoryImpl(final AmazonS3 client, final DataAccessProperties properties) {
		this.client = checkNotNull(client, "client cannot be null");
		this.properties = checkNotNull(properties, "properties cannot be null");
	}

	@Override
	public Optional<S3Object> getObject(final String key) {
		checkNotNull(key, "key cannot be null");
		checkArgument(!key.trim().isEmpty(), "key cannot be empty");

		if (properties.getExcludes().contains(key)) {
			return Optional.empty();
		}

		if (client.doesObjectExist(properties.getBucketName(), key)) {
			return Optional.of(client.getObject(new GetObjectRequest(properties.getBucketName(), key)));
		}
		return Optional.empty();
	}

	@Override
	public List<S3ObjectSummary> getObjectSummaries(final String prefix) {
		checkNotNull(prefix, "prefix cannot be null");

		final ListObjectsV2Request request = new ListObjectsV2Request()
				.withBucketName(properties.getBucketName())
				.withPrefix(prefix)
				.withMaxKeys(properties.getMaxKeys());

		final List<S3ObjectSummary> summaries = new ArrayList<>();
		ListObjectsV2Result result;
		do {
			result = client.listObjectsV2(request);
			summaries.addAll(result.getObjectSummaries());
			request.setContinuationToken(result.getNextContinuationToken());
		} while (result.isTruncated());

		return summaries.stream().filter(s -> !properties.getExcludes().contains(s.getKey())).collect(Collectors.toList());
	}

	@Override
	public String getHostingUrl(final String key) {
		final String hostingUrlFormat = properties.getObjectUrlFormat();
		return MessageFormat.format(hostingUrlFormat, properties.getBucketName(), key == null ? "" : key);
	}

}
