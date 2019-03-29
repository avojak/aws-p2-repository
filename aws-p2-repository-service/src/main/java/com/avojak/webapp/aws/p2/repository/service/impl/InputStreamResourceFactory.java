package com.avojak.webapp.aws.p2.repository.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

/**
 * Factory class to create instances of {@link InputStreamResource}.
 */
@Component
public class InputStreamResourceFactory {

	/**
	 * Creates a new input stream resource for the given S3 object input stream.
	 *
	 * @param s3ObjectInputStream
	 * 		The {@link S3ObjectInputStream}.
	 *
	 * @return The new, non-null {@link InputStreamResource}.
	 */
	public InputStreamResource create(final S3ObjectInputStream s3ObjectInputStream) {
		return new InputStreamResource(s3ObjectInputStream);
	}

}
