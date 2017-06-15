package io.trane.ndbc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.trane.ndbc.value.Value;

public final class Row {

  private final Map<String, Integer> positions;
  private final Value<?>[] columns;

  public static final Row apply(final Map<String, Integer> positions, final Value<?>[] columns) {
    return new Row(positions, columns);
  }

  private Row(final Map<String, Integer> positions, final Value<?>[] columns) {
    super();
    this.positions = positions;
    this.columns = columns;
  }

  public final Value<?> column(final int columnPosition) {
    return columns[columnPosition];
  }

  public final Value<?> column(final String columnName) {
    return columns[positions.get(columnName)];
  }

  public final Iterable<String> columnNames() {
    return Collections.unmodifiableList(positions.entrySet().stream().sorted(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey).collect(Collectors.toList()));
  }

  public final List<Value<?>> columns() {
    return Collections.unmodifiableList(Arrays.asList(columns));
  }
}
