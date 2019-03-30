package com.avojak.webapp.aws.p2.repository.dataaccess.repository;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;
import java.util.Optional;

/**
 * Provides methods to interact with a AWS S3 bucket.
 */
public interface S3BucketRepository {

	/**
	 * Retrieves an object from a bucket.
	 *
	 * @param key
	 * 		The key which refers to the object. Cannot be null or empty.
	 *
	 * @return The non-null {@link Optional} of the {@link S3Object}.
	 */
	Optional<S3Object> getObject(final String key);

	/**
	 * Retrieves object summaries for the given prefix.
	 *
	 * @param prefix
	 * 		The object prefix. Cannot be null.
	 *
	 * @return The non-null, possibly empty List of {@link S3ObjectSummary} objects.
	 */
	List<S3ObjectSummary> getObjectSummaries(final String prefix);

	/**
	 * Gets the hosting URL for the object with the given key.
	 *
	 * @param key
	 * 		The object key. Cannot be null.
	 *
	 * @return The non-null hosting URL.
	 */
	String getHostingUrl(final String key);

}
