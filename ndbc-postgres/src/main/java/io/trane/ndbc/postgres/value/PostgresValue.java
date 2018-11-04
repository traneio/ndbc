package io.trane.ndbc.postgres.value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.UUID;

import io.trane.ndbc.value.Value;

public abstract class PostgresValue<T> extends Value<T> {

  public PostgresValue(final T value) {
    super(value);
  }

  public Character[] getCharacterArray() {
    return cantRead("Character[]");
  }

  public String[] getStringArray() {
    return cantRead("String[]");
  }

  public Integer[] getIntegerArray() {
    return cantRead("Integer[]");
  }

  public Boolean[] getBooleanArray() {
    return cantRead("Boolean[]");
  }

  public Long[] getLongArray() {
    return cantRead("Long[]");
  }

  public Short[] getShortArray() {
    return cantRead("Short[]");
  }

  public BigDecimal[] getBigDecimalArray() {
    return cantRead("BigDecimal[]");
  }

  public Float[] getFloatArray() {
    return cantRead("Float[]");
  }

  public Double[] getDoubleArray() {
    return cantRead("Double[]");
  }

  public LocalDateTime[] getLocalDateTimeArray() {
    return cantRead("LocalDateTime[]");
  }

  public byte[][] getByteArrayArray() {
    return cantRead("byte[][]");
  }

  public LocalDate[] getLocalDateArray() {
    return cantRead("LocalDate[]");
  }

  public LocalTime[] getLocalTimeArray() {
    return cantRead("LocalTime[]");
  }

  @Override
  public OffsetTime getOffsetTime() {
    return cantRead("OffsetTime");
  }

  public OffsetTime[] getOffsetTimeArray() {
    return cantRead("OffsetTime[]");
  }

  public UUID[] getUUIDArray() {
    return cantRead("UUID[]");
  }

}
