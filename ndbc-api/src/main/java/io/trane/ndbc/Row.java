package io.trane.ndbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import io.trane.ndbc.value.Value;

/**
 * Represents a database row in memory. Supports name- and index-based column
 * referencing
 */
public class Row {

  protected final Map<String, Integer> positions;
  protected final Value<?>[]           columns;

  public static Row create(final Map<String, Integer> positions, final Value<?>[] columns) {
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

  // index-based

  public boolean isNull(final int columnPosition) {
    return column(columnPosition).isNull();
  }

  public Character getCharacter(final int columnPosition) {
    return column(columnPosition).getCharacter();
  }

  public String getString(final int columnPosition) {
    return column(columnPosition).getString();
  }

  public Integer getInteger(final int columnPosition) {
    return column(columnPosition).getInteger();
  }

  public Boolean getBoolean(final int columnPosition) {
    return column(columnPosition).getBoolean();
  }

  public Long getLong(final int columnPosition) {
    return column(columnPosition).getLong();
  }

  public Byte getByte(final int columnPosition) {
    return column(columnPosition).getByte();
  }

  public Short getShort(final int columnPosition) {
    return column(columnPosition).getShort();
  }

  public BigDecimal getBigDecimal(final int columnPosition) {
    return column(columnPosition).getBigDecimal();
  }

  public Float getFloat(final int columnPosition) {
    return column(columnPosition).getFloat();
  }

  public Double getDouble(final int columnPosition) {
    return column(columnPosition).getDouble();
  }

  public LocalDateTime getLocalDateTime(final int columnPosition) {
    return column(columnPosition).getLocalDateTime();
  }

  public byte[] getByteArray(final int columnPosition) {
    return column(columnPosition).getByteArray();
  }

  public LocalDate getLocalDate(final int columnPosition) {
    return column(columnPosition).getLocalDate();
  }

  public LocalTime getLocalTime(final int columnPosition) {
    return column(columnPosition).getLocalTime();
  }

  public OffsetTime getOffsetTime(final int columnPosition) {
    return column(columnPosition).getOffsetTime();
  }

  public UUID getUUID(final int columnPosition) {
    return column(columnPosition).getUUID();
  }

  // name-based

  public boolean isNull(final String columnName) {
    return column(columnName).isNull();
  }

  public Character getCharacter(final String columnName) {
    return column(columnName).getCharacter();
  }

  public String getString(final String columnName) {
    return column(columnName).getString();
  }

  public Integer getInteger(final String columnName) {
    return column(columnName).getInteger();
  }

  public Boolean getBoolean(final String columnName) {
    return column(columnName).getBoolean();
  }

  public Long getLong(final String columnName) {
    return column(columnName).getLong();
  }

  public Byte getByte(final String columnName) {
    return column(columnName).getByte();
  }

  public Short getShort(final String columnName) {
    return column(columnName).getShort();
  }

  public BigDecimal getBigDecimal(final String columnName) {
    return column(columnName).getBigDecimal();
  }

  public Float getFloat(final String columnName) {
    return column(columnName).getFloat();
  }

  public Double getDouble(final String columnName) {
    return column(columnName).getDouble();
  }

  public LocalDateTime getLocalDateTime(final String columnName) {
    return column(columnName).getLocalDateTime();
  }

  public byte[] getByteArray(final String columnName) {
    return column(columnName).getByteArray();
  }

  public LocalDate getLocalDate(final String columnName) {
    return column(columnName).getLocalDate();
  }

  public LocalTime getLocalTime(final String columnName) {
    return column(columnName).getLocalTime();
  }

  public OffsetTime getOffsetTime(final String columnName) {
    return column(columnName).getOffsetTime();
  }

  public UUID getUUID(final String columnName) {
    return column(columnName).getUUID();
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
