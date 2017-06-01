package io.trane.ndbc.util;

import java.util.HashSet;
import java.util.Set;

public class Collections {

  @SafeVarargs
  public static <T> Set<T> toImmutableSet(T... items) {
    Set<T> set = new HashSet<>();
    for (T item : items)
      set.add(item);
    return java.util.Collections.unmodifiableSet(set);
  }
}
