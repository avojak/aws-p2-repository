package com.avojak.webapp.aws.p2.repository.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link Qualifier}.
 */
public class QualifierTest {

	@Test(expected = NullPointerException.class)
	public void testFromPathElement_NullPathElement() {
		Qualifier.fromPathElement(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFromPathElement_EmptyPathElement() {
		Qualifier.fromPathElement(" ");
	}

	@Test
	public void testFromPathElement_Snapshots() {
		assertEquals(Qualifier.SNAPSHOT, Qualifier.fromPathElement("snapshots"));
	}

	@Test
	public void testFromPathElement_Releases() {
		assertEquals(Qualifier.RELEASE, Qualifier.fromPathElement("releases"));
	}

	@Test
	public void testFromPathElement_NoQualifierFound() {
		try {
			Qualifier.fromPathElement("foobar");
		} catch (final IllegalArgumentException e) {
			assertEquals("No Qualifier found for path element: foobar", e.getMessage());
		}
	}

}
