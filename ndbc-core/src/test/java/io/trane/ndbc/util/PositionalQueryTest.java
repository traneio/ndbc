package io.trane.ndbc.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PositionalQueryTest {

  @Test
  public void noParams() {
    String query = "SELECT * FROM test";
    assertEquals(PositionalQuery.apply(query), query);
  }

  @Test
  public void withParams() {
    String query = "SELECT ?, * FROM test WHERE a = ?";
    String positional = PositionalQuery.apply(query);
    assertEquals(positional, "SELECT $1, * FROM test WHERE a = $2");
  }

  @Test
  public void escapeQuestionMark() {
    String query = "SELECT ??, * FROM test WHERE a = ?";
    String positional = PositionalQuery.apply(query);
    assertEquals(positional, "SELECT ?, * FROM test WHERE a = $1");
  }

}
