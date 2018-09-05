package io.trane.ndbc;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.LongValue;
import io.trane.ndbc.value.Value;

public class RowTest {

  Map<String, Integer> positions = new HashMap<>();
  IntegerValue         v1        = new IntegerValue(1);
  LongValue            v2        = new LongValue(2L);
  Value<?>[]           columns   = { v1, v2 };

  Row row = Row.apply(positions, columns);

  public RowTest() {
    positions.put("a", 0);
    positions.put("b", 1);
  }

  @Test
  public void columnPos() {
    assertEquals(v1, row.column(0));
    assertEquals(v2, row.column(1));
  }

  @Test
  public void columnName() {
    assertEquals(v1, row.column("a"));
    assertEquals(v2, row.column("b"));
  }

  @Test
  public void columnNames() {
    final Iterator<String> it = row.columnNames().iterator();
    assertEquals("a", it.next());
    assertEquals("b", it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void columns() {
    assertArrayEquals(columns, row.columns().toArray());
  }
}
