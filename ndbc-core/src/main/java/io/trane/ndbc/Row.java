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

  public Row(Map<String, Integer> positions, Value<?>[] columns) {
    super();
    this.positions = positions;
    this.columns = columns;
  }

  public Value<?> column(int columnPosition) {
    return columns[columnPosition];
  }

  public Value<?> column(String columnName) {
    return columns[positions.get(columnName)];
  }

  public List<String> columnNames() {
    return Collections.unmodifiableList(positions.entrySet().stream().sorted(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey).collect(Collectors.toList()));
  }

  public List<Value<?>> columns() {
    return Collections.unmodifiableList(Arrays.asList(columns));
  }
}
