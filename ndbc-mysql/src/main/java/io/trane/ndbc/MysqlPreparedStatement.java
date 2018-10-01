package io.trane.ndbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

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

public final class MysqlPreparedStatement extends PreparedStatement {

  public static MysqlPreparedStatement apply(final PreparedStatement ps) {
    return new MysqlPreparedStatement(ps.query, ps.params);
  }

  public static MysqlPreparedStatement apply(final String query) {
    return new MysqlPreparedStatement(query);
  }

  private MysqlPreparedStatement(String query) {
    super(query);
  }

  private MysqlPreparedStatement(String query, Value<?>[] params) {
    super(query, params);
  }

  // Overrides

  @Override
  public final MysqlPreparedStatement setBigDecimal(final BigDecimal value) {
    return setBigDecimal(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setBigDecimal(final int index, final BigDecimal value) {
    return set(index, value == null ? Value.NULL : new BigDecimalValue(value));
  }

  @Override
  public final MysqlPreparedStatement setBoolean(final Boolean value) {
    return setBoolean(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setBoolean(final int index, final Boolean value) {
    return set(index, value == null ? Value.NULL : new BooleanValue(value));
  }

  @Override
  public final MysqlPreparedStatement setByteArray(final byte[] value) {
    return setByteArray(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setByteArray(final int index, final byte[] value) {
    return set(index, value == null ? Value.NULL : new ByteArrayValue(value));
  }

  @Override
  public final MysqlPreparedStatement setDouble(final Double value) {
    return setDouble(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setDouble(final int index, final Double value) {
    return set(index, value == null ? Value.NULL : new DoubleValue(value));
  }

  @Override
  public final MysqlPreparedStatement setFloat(final Float value) {
    return setFloat(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setFloat(final int index, final Float value) {
    return set(index, value == null ? Value.NULL : new FloatValue(value));
  }

  @Override
  public final MysqlPreparedStatement setInteger(final Integer value) {
    return setInteger(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setInteger(final int index, final Integer value) {
    return set(index, value == null ? Value.NULL : new IntegerValue(value));
  }

  @Override
  public final MysqlPreparedStatement setLocalDate(final LocalDate value) {
    return setLocalDate(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setLocalDate(final int index, final LocalDate value) {
    return set(index, value == null ? Value.NULL : new LocalDateValue(value));
  }

  @Override
  public final MysqlPreparedStatement setLocalDateTime(final LocalDateTime value) {
    return setLocalDateTime(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setLocalDateTime(final int index, final LocalDateTime value) {
    return set(index, value == null ? Value.NULL : new LocalDateTimeValue(value));
  }

  @Override
  public final MysqlPreparedStatement setLocalTime(final LocalTime value) {
    return setLocalTime(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setLocalTime(final int index, final LocalTime value) {
    return set(index, value == null ? Value.NULL : new LocalTimeValue(value));
  }

  @Override
  public final MysqlPreparedStatement setLong(final Long value) {
    return setLong(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setLong(final int index, final Long value) {
    return set(index, value == null ? Value.NULL : new LongValue(value));
  }

  @Override
  public final MysqlPreparedStatement setByte(final Byte value) {
    return setByte(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setByte(final int index, final Byte value) {
    return set(index, value == null ? Value.NULL : new ByteValue(value));
  }

  @Override
  public final MysqlPreparedStatement setShort(final Short value) {
    return setShort(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setShort(final int index, final Short value) {
    return set(index, value == null ? Value.NULL : new ShortValue(value));
  }

  @Override
  public final MysqlPreparedStatement setString(final String value) {
    return setString(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setString(final int index, final String value) {
    return set(index, value == null ? Value.NULL : new StringValue(value));
  }

  @Override
  public final MysqlPreparedStatement setUUID(final int index, final UUID value) {
    return set(index, value == null ? Value.NULL : new UUIDValue(value));
  }

  @Override
  public final MysqlPreparedStatement setUUID(final UUID value) {
    return setUUID(nextIndex(), value);
  }

  @Override
  public final MysqlPreparedStatement setNull() {
    return setNull(nextIndex());
  }

  @Override
  public final MysqlPreparedStatement setNull(final int index) {
    return set(index, Value.NULL);
  }

  @Override
  public final MysqlPreparedStatement set(final Value<?> param) {
    return set(nextIndex(), param);
  }

  @Override
  public final MysqlPreparedStatement set(final int index, final Value<?> param) {
    return apply(super.set(index, param));
  }
}
