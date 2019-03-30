package com.avojak.webapp.aws.p2.repository.service.impl;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Test class for {@link InputStreamResourceFactory}.
 */
public class InputStreamResourceFactoryTest {

	/**
	 * Tests that {@link InputStreamResourceFactory#create(S3ObjectInputStream)} creates a non-null {@link
	 * org.springframework.core.io.InputStreamResource}.
	 */
	@Test
	public void testCreate() {
		final S3ObjectInputStream s3ObjectInputStream = mock(S3ObjectInputStream.class);
		assertNotNull(new InputStreamResourceFactory().create(s3ObjectInputStream));
	}

}
