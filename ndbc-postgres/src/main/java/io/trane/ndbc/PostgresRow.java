package io.trane.ndbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Map;
import java.util.UUID;

import io.trane.ndbc.postgres.value.PostgresValue;
import io.trane.ndbc.value.Value;

public class PostgresRow extends Row {

  public static PostgresRow apply(final Row row) {
    return apply(row.positions, row.columns);
  }

  public static PostgresRow apply(final Map<String, Integer> positions, final Value<?>[] columns) {
    return new PostgresRow(positions, columns);
  }

  protected PostgresRow(final Map<String, Integer> positions, final Value<?>[] columns) {
    super(positions, columns);
  }

  // index-based

  public Character[] getCharacterArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getCharacterArray();
    else
      return cantRead(v, "Character[]");
  }

  public String[] getStringArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getStringArray();
    else
      return cantRead(v, "String[]");
  }

  public Integer[] getIntegerArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getIntegerArray();
    else
      return cantRead(v, "Integer[]");
  }

  public Boolean[] getBooleanArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getBooleanArray();
    else
      return cantRead(v, "Boolean[]");
  }

  public Long[] getLongArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getLongArray();
    else
      return cantRead(v, "Long[]");
  }

  public Short[] getShortArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getShortArray();
    else
      return cantRead(v, "Short[]");
  }

  public BigDecimal[] getBigDecimalArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getBigDecimalArray();
    else
      return cantRead(v, "BigDecimal[]");
  }

  public Float[] getFloatArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getFloatArray();
    else
      return cantRead(v, "Float[]");
  }

  public Double[] getDoubleArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getDoubleArray();
    else
      return cantRead(v, "Double[]");
  }

  public LocalDateTime[] getLocalDateTimeArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getLocalDateTimeArray();
    else
      return cantRead(v, "LocalDateTime[]");
  }

  public byte[][] getByteArrayArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getByteArrayArray();
    else
      return cantRead(v, "byte[][]");
  }

  public LocalDate[] getLocalDateArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getLocalDateArray();
    else
      return cantRead(v, "LocalDate[]");
  }

  public LocalTime[] getLocalTimeArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getLocalTimeArray();
    else
      return cantRead(v, "LocalTime[]");
  }

  public OffsetTime[] getOffsetTimeArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getOffsetTimeArray();
    else
      return cantRead(v, "Offset[]");
  }

  public UUID[] getUUIDArray(final int index) {
    final Value<?> v = column(index);
    if (v instanceof PostgresValue)
      return ((PostgresValue<?>) v).getUUIDArray();
    else
      return cantRead(v, "UUID[]");
  }

  // name-based

  public Character[] getCharacterArray(final String column) {
    return getCharacterArray(positions.get(column));
  }

  public String[] getStringArray(final String column) {
    return getStringArray(positions.get(column));
  }

  public Integer[] getIntegerArray(final String column) {
    return getIntegerArray(positions.get(column));
  }

  public Boolean[] getBooleanArray(final String column) {
    return getBooleanArray(positions.get(column));
  }

  public Long[] getLongArray(final String column) {
    return getLongArray(positions.get(column));
  }

  public Short[] getShortArray(final String column) {
    return getShortArray(positions.get(column));
  }

  public BigDecimal[] getBigDecimalArray(final String column) {
    return getBigDecimalArray(positions.get(column));
  }

  public Float[] getFloatArray(final String column) {
    return getFloatArray(positions.get(column));
  }

  public Double[] getDoubleArray(final String column) {
    return getDoubleArray(positions.get(column));
  }

  public LocalDateTime[] getLocalDateTimeArray(final String column) {
    return getLocalDateTimeArray(positions.get(column));
  }

  public byte[][] getByteArrayArray(final String column) {
    return getByteArrayArray(positions.get(column));
  }

  public LocalDate[] getLocalDateArray(final String column) {
    return getLocalDateArray(positions.get(column));
  }

  public LocalTime[] getLocalTimeArray(final String column) {
    return getLocalTimeArray(positions.get(column));
  }

  public OffsetTime[] getOffsetTimeArray(final String column) {
    return getOffsetTimeArray(positions.get(column));
  }

  public UUID[] getUUIDArray(final String column) {
    return getUUIDArray(positions.get(column));
  }

  private final <U> U cantRead(final Value<?> v, final String type) {
    throw new UnsupportedOperationException("Can't read `" + v + "` as `" + type
        + "`");
  }
}
