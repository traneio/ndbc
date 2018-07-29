package io.trane.ndbc.util;

import java.util.HashSet;
import java.util.Set;

public final class Collections {

	@SafeVarargs
	public static final <T> Set<T> toImmutableSet(final T... items) {
		final Set<T> set = new HashSet<>();
		for (final T item : items)
			set.add(item);
		return java.util.Collections.unmodifiableSet(set);
	}
}
