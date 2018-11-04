package io.trane.ndbc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PositionalQueryTest {

  @Test
  public void noParams() {
    final String query = "SELECT * FROM test";
    assertEquals(PositionalQuery.apply(query), query);
  }

  @Test
  public void withParams() {
    final String query = "SELECT ?, * FROM test WHERE a = ?";
    final String positional = PositionalQuery.apply(query);
    assertEquals(positional, "SELECT $1, * FROM test WHERE a = $2");
  }

  @Test
  public void escapeQuestionMark() {
    final String query = "SELECT ??, * FROM test WHERE a = ?";
    final String positional = PositionalQuery.apply(query);
    assertEquals(positional, "SELECT ?, * FROM test WHERE a = $1");
  }

}
