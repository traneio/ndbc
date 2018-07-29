package io.trane.ndbc.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

public class CollectionsTest {

	@Test
	public void toImmutableSet() {
		final Set<Integer> set = Collections.toImmutableSet(1, 2, 3);
		assertEquals(3, set.size());
		assertTrue(set.contains(1));
		assertTrue(set.contains(2));
		assertTrue(set.contains(3));
	}

	@Test(expected = UnsupportedOperationException.class)
	public void toImmutableSetUnmodifiable() {
		final Set<Integer> set = Collections.toImmutableSet(1, 2, 3);
		set.add(4);
	}
}
