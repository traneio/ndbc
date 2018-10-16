package io.trane.ndbc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.trane.ndbc.value.Value;

public class Row {

  protected final Map<String, Integer> positions;
  protected final Value<?>[]           columns;

  public static Row apply(final Map<String, Integer> positions, final Value<?>[] columns) {
    return new Row(positions, columns);
  }

  protected Row(final Map<String, Integer> positions, final Value<?>[] columns) {
    this.positions = positions;
    this.columns = columns;
  }

  public final Value<?> column(final int columnPosition) {
    return columns[columnPosition];
  }

  public final Value<?> column(final String columnName) {
    return columns[positions.get(columnName)];
  }

  public final List<String> columnNames() {
    return Collections.unmodifiableList(positions.entrySet().stream().sorted(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey).collect(Collectors.toList()));
  }

  public final List<Value<?>> columns() {
    return Collections.unmodifiableList(Arrays.asList(columns));
  }

  @Override
  public String toString() {
    final Iterator<String> names = columnNames().iterator();
    final Iterator<Value<?>> values = Arrays.asList(columns).iterator();
    String result = "Row [";
    while (names.hasNext()) {
      final String name = names.next();
      final Value<?> value = values.next();
      result += name + "= " + value.get();
      if (names.hasNext())
        result += ", ";
    }
    return result + "]";
  }
}
