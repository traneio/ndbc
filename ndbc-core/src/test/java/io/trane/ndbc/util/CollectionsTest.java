package io.trane.ndbc.util;

import java.util.Set;

import org.junit.Test;
import static org.junit.Assert.*;

public class CollectionsTest {

  @Test
  public void toImmutableSet() {
    Set<Integer> set = Collections.toImmutableSet(1, 2, 3);
    assertEquals(3, set.size());
    assertTrue(set.contains(1));
    assertTrue(set.contains(2));
    assertTrue(set.contains(3));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void toImmutableSetUnmodifiable() {
    Set<Integer> set = Collections.toImmutableSet(1, 2, 3);
    set.add(4);
  }
}
