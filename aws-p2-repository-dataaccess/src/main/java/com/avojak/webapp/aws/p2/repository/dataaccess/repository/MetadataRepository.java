package com.avojak.webapp.aws.p2.repository.dataaccess.repository;

import com.avojak.webapp.aws.p2.repository.model.repository.P2Repository;

/**
 * Provides methods to retrieve metadata of p2 repositories.
 */
public interface MetadataRepository {

	P2Repository getMetadata(final String url);

}
