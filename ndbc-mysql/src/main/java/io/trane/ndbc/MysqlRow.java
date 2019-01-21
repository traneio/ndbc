package io.trane.ndbc;

import java.util.Map;

import io.trane.ndbc.value.Value;

public class MysqlRow extends Row {

  public static MysqlRow create(final Row row) {
    return create(row.positions, row.columns);
  }

  public static MysqlRow create(final Map<String, Integer> positions, final Value<?>[] columns) {
    return new MysqlRow(positions, columns);
  }

  protected MysqlRow(final Map<String, Integer> positions, final Value<?>[] columns) {
    super(positions, columns);
  }
}
