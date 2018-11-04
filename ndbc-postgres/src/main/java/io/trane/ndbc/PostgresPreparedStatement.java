package io.trane.ndbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.UUID;

import io.trane.ndbc.postgres.value.BigDecimalArrayValue;
import io.trane.ndbc.postgres.value.BooleanArrayValue;
import io.trane.ndbc.postgres.value.ByteArrayArrayValue;
import io.trane.ndbc.postgres.value.DoubleArrayValue;
import io.trane.ndbc.postgres.value.FloatArrayValue;
import io.trane.ndbc.postgres.value.IntegerArrayValue;
import io.trane.ndbc.postgres.value.LocalDateArrayValue;
import io.trane.ndbc.postgres.value.LocalDateTimeArrayValue;
import io.trane.ndbc.postgres.value.LocalTimeArrayValue;
import io.trane.ndbc.postgres.value.LongArrayValue;
import io.trane.ndbc.postgres.value.OffsetTimeArrayValue;
import io.trane.ndbc.postgres.value.OffsetTimeValue;
import io.trane.ndbc.postgres.value.ShortArrayValue;
import io.trane.ndbc.postgres.value.StringArrayValue;
import io.trane.ndbc.postgres.value.UUIDArrayValue;
import io.trane.ndbc.value.BigDecimalValue;
import io.trane.ndbc.value.BooleanValue;
import io.trane.ndbc.value.ByteArrayValue;
import io.trane.ndbc.value.ByteValue;
import io.trane.ndbc.value.DoubleValue;
import io.trane.ndbc.value.FloatValue;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.LocalDateTimeValue;
import io.trane.ndbc.value.LocalDateValue;
import io.trane.ndbc.value.LocalTimeValue;
import io.trane.ndbc.value.LongValue;
import io.trane.ndbc.value.ShortValue;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.UUIDValue;
import io.trane.ndbc.value.Value;

public final class PostgresPreparedStatement extends PreparedStatement {

  public static PostgresPreparedStatement create(final PreparedStatement ps) {
    return new PostgresPreparedStatement(ps.query, ps.params);
  }

  public static PostgresPreparedStatement create(final String query) {
    return new PostgresPreparedStatement(query);
  }

  private PostgresPreparedStatement(final String query) {
    super(query);
  }

  private PostgresPreparedStatement(final String query, final Value<?>[] params) {
    super(query, params);
  }

  // Custom types

  public final PostgresPreparedStatement setOffsetTime(final OffsetTime value) {
    return setOffsetTime(nextIndex(), value);
  }

  public final PostgresPreparedStatement setOffsetTime(final int index, final OffsetTime value) {
    return set(index, value == null ? Value.NULL : new OffsetTimeValue(value));
  }

  // Arrays

  public final PostgresPreparedStatement setOffsetTimeArray(final OffsetTime[] value) {
    return setOffsetTimeArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setOffsetTimeArray(final int index, final OffsetTime[] value) {
    return set(index, value == null ? Value.NULL : new OffsetTimeArrayValue(value));
  }

  public final PostgresPreparedStatement setBigDecimalArray(final BigDecimal[] value) {
    return setBigDecimalArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setBigDecimalArray(final int index, final BigDecimal[] value) {
    return set(index, value == null ? Value.NULL : new BigDecimalArrayValue(value));
  }

  public final PostgresPreparedStatement setBooleanArray(final Boolean[] value) {
    return setBooleanArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setBooleanArray(final int index, final Boolean[] value) {
    return set(index, value == null ? Value.NULL : new BooleanArrayValue(value));
  }

  public final PostgresPreparedStatement setByteArrayArray(final byte[][] value) {
    return setByteArrayArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setByteArrayArray(final int index, final byte[][] value) {
    return set(index, value == null ? Value.NULL : new ByteArrayArrayValue(value));
  }

  public final PostgresPreparedStatement setDoubleArray(final Double[] value) {
    return setDoubleArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setDoubleArray(final int index, final Double[] value) {
    return set(index, value == null ? Value.NULL : new DoubleArrayValue(value));
  }

  public final PostgresPreparedStatement setFloatArray(final Float[] value) {
    return setFloatArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setFloatArray(final int index, final Float[] value) {
    return set(index, value == null ? Value.NULL : new FloatArrayValue(value));
  }

  public final PostgresPreparedStatement setIntegerArray(final Integer[] value) {
    return setIntegerArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setIntegerArray(final int index, final Integer[] value) {
    return set(index, value == null ? Value.NULL : new IntegerArrayValue(value));
  }

  public final PostgresPreparedStatement setLocalDateArray(final LocalDate[] value) {
    return setLocalDateArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setLocalDateArray(final int index, final LocalDate[] value) {
    return set(index, value == null ? Value.NULL : new LocalDateArrayValue(value));
  }

  public final PostgresPreparedStatement setLocalDateTimeArray(final LocalDateTime[] value) {
    return setLocalDateTimeArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setLocalDateTimeArray(final int index, final LocalDateTime[] value) {
    return set(index, value == null ? Value.NULL : new LocalDateTimeArrayValue(value));
  }

  public final PostgresPreparedStatement setLocalTimeArray(final LocalTime[] value) {
    return setLocalTimeArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setLocalTimeArray(final int index, final LocalTime[] value) {
    return set(index, value == null ? Value.NULL : new LocalTimeArrayValue(value));
  }

  public final PostgresPreparedStatement setLongArray(final Long[] value) {
    return setLongArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setLongArray(final int index, final Long[] value) {
    return set(index, value == null ? Value.NULL : new LongArrayValue(value));
  }

  public final PostgresPreparedStatement setShortArray(final Short[] value) {
    return setShortArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setShortArray(final int index, final Short[] value) {
    return set(index, value == null ? Value.NULL : new ShortArrayValue(value));
  }

  public final PostgresPreparedStatement setStringArray(final String[] value) {
    return setStringArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setStringArray(final int index, final String[] value) {
    return set(index, value == null ? Value.NULL : new StringArrayValue(value));
  }

  public final PostgresPreparedStatement setUUIDArray(final UUID[] value) {
    return setUUIDArray(nextIndex(), value);
  }

  public final PostgresPreparedStatement setUUIDArray(final int index, final UUID[] value) {
    return set(index, value == null ? Value.NULL : new UUIDArrayValue(value));
  }

  // Overrides

  @Override
  public final PostgresPreparedStatement setBigDecimal(final BigDecimal value) {
    return setBigDecimal(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setBigDecimal(final int index, final BigDecimal value) {
    return set(index, value == null ? Value.NULL : new BigDecimalValue(value));
  }

  @Override
  public final PostgresPreparedStatement setBoolean(final Boolean value) {
    return setBoolean(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setBoolean(final int index, final Boolean value) {
    return set(index, value == null ? Value.NULL : new BooleanValue(value));
  }

  @Override
  public final PostgresPreparedStatement setByteArray(final byte[] value) {
    return setByteArray(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setByteArray(final int index, final byte[] value) {
    return set(index, value == null ? Value.NULL : new ByteArrayValue(value));
  }

  @Override
  public final PostgresPreparedStatement setDouble(final Double value) {
    return setDouble(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setDouble(final int index, final Double value) {
    return set(index, value == null ? Value.NULL : new DoubleValue(value));
  }

  @Override
  public final PostgresPreparedStatement setFloat(final Float value) {
    return setFloat(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setFloat(final int index, final Float value) {
    return set(index, value == null ? Value.NULL : new FloatValue(value));
  }

  @Override
  public final PostgresPreparedStatement setInteger(final Integer value) {
    return setInteger(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setInteger(final int index, final Integer value) {
    return set(index, value == null ? Value.NULL : new IntegerValue(value));
  }

  @Override
  public final PostgresPreparedStatement setLocalDate(final LocalDate value) {
    return setLocalDate(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setLocalDate(final int index, final LocalDate value) {
    return set(index, value == null ? Value.NULL : new LocalDateValue(value));
  }

  @Override
  public final PostgresPreparedStatement setLocalDateTime(final LocalDateTime value) {
    return setLocalDateTime(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setLocalDateTime(final int index, final LocalDateTime value) {
    return set(index, value == null ? Value.NULL : new LocalDateTimeValue(value));
  }

  @Override
  public final PostgresPreparedStatement setLocalTime(final LocalTime value) {
    return setLocalTime(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setLocalTime(final int index, final LocalTime value) {
    return set(index, value == null ? Value.NULL : new LocalTimeValue(value));
  }

  @Override
  public final PostgresPreparedStatement setLong(final Long value) {
    return setLong(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setLong(final int index, final Long value) {
    return set(index, value == null ? Value.NULL : new LongValue(value));
  }

  @Override
  public final PostgresPreparedStatement setByte(final Byte value) {
    return setByte(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setByte(final int index, final Byte value) {
    return set(index, value == null ? Value.NULL : new ByteValue(value));
  }

  @Override
  public final PostgresPreparedStatement setShort(final Short value) {
    return setShort(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setShort(final int index, final Short value) {
    return set(index, value == null ? Value.NULL : new ShortValue(value));
  }

  @Override
  public final PostgresPreparedStatement setString(final String value) {
    return setString(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setString(final int index, final String value) {
    return set(index, value == null ? Value.NULL : new StringValue(value));
  }

  @Override
  public final PostgresPreparedStatement setUUID(final int index, final UUID value) {
    return set(index, value == null ? Value.NULL : new UUIDValue(value));
  }

  @Override
  public final PostgresPreparedStatement setUUID(final UUID value) {
    return setUUID(nextIndex(), value);
  }

  @Override
  public final PostgresPreparedStatement setNull() {
    return setNull(nextIndex());
  }

  @Override
  public final PostgresPreparedStatement setNull(final int index) {
    return set(index, Value.NULL);
  }

  @Override
  public final PostgresPreparedStatement set(final Value<?> param) {
    return set(nextIndex(), param);
  }

  @Override
  public final PostgresPreparedStatement set(final int index, final Value<?> param) {
    return create(super.set(index, param));
  }
}
