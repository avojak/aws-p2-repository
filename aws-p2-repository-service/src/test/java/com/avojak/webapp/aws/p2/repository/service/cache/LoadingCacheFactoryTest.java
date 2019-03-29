package com.avojak.webapp.aws.p2.repository.service.cache;

import com.avojak.webapp.aws.p2.repository.service.configuration.ServiceProperties;
import com.google.common.base.Ticker;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNotNull;

/**
 * Test class for {@link LoadingCacheFactory}.
 */
@RunWith(MockitoJUnitRunner.class)
public class LoadingCacheFactoryTest {

	@Mock
	private ProjectCacheLoader projectCacheLoader;

	@Mock
	private Ticker ticker;

	private ServiceProperties properties;

	@Before
	public void setup() {
		properties = new ServiceProperties(30000L, TimeUnit.SECONDS, "{0}", "{0}", "{0}", "p2.avojak.com");
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullCacheLoader() {
		new LoadingCacheFactory(null, ticker, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullTicker() {
		new LoadingCacheFactory(projectCacheLoader, null, properties);
	}

	@Test(expected = NullPointerException.class)
	public void testConstructor_NullServiceProperties() {
		new LoadingCacheFactory(projectCacheLoader, ticker, null);
	}

	@Test
	public void testCreate() {
		assertNotNull(new LoadingCacheFactory(projectCacheLoader, ticker, properties).create());
	}

}
