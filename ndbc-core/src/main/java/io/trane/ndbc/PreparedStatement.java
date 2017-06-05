package io.trane.ndbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.trane.ndbc.value.BigDecimalValue;
import io.trane.ndbc.value.BooleanValue;
import io.trane.ndbc.value.ByteArrayValue;
import io.trane.ndbc.value.DoubleValue;
import io.trane.ndbc.value.FloatValue;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.LocalDateTimeValue;
import io.trane.ndbc.value.LocalDateValue;
import io.trane.ndbc.value.LocalTimeValue;
import io.trane.ndbc.value.LongValue;
import io.trane.ndbc.value.OffsetTimeValue;
import io.trane.ndbc.value.ShortValue;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.Value;

public final class PreparedStatement {

  private static final Value<?>[] emptyValues = new Value<?>[0];

  public static final PreparedStatement apply(final String query) {
    return new PreparedStatement(query, emptyValues);
  }

  private final String query;
  private final Value<?>[] params;

  private PreparedStatement(final String query, final Value<?>[] params) {
    super();
    this.query = query;
    this.params = params;
  }

  public final PreparedStatement bindBigDecimal(final BigDecimal value) {
    return bind(value == null ? Value.NULL : new BigDecimalValue(value));
  }

  public final PreparedStatement bindBoolean(final Boolean value) {
    return bind(value == null ? Value.NULL : new BooleanValue(value));
  }

  public final PreparedStatement bindByteArray(final byte[] value) {
    return bind(value == null ? Value.NULL : new ByteArrayValue(value));
  }

  public final PreparedStatement bindDouble(final Double value) {
    return bind(value == null ? Value.NULL : new DoubleValue(value));
  }

  public final PreparedStatement bindFloat(final Float value) {
    return bind(value == null ? Value.NULL : new FloatValue(value));
  }

  public final PreparedStatement bindInteger(final Integer value) {
    return bind(value == null ? Value.NULL : new IntegerValue(value));
  }

  public final PreparedStatement bindLocalDate(final LocalDate value) {
    return bind(value == null ? Value.NULL : new LocalDateValue(value));
  }

  public final PreparedStatement bindLocalDateTime(final LocalDateTime value) {
    return bind(value == null ? Value.NULL : new LocalDateTimeValue(value));
  }

  public final PreparedStatement bindLocalTime(final LocalTime value) {
    return bind(value == null ? Value.NULL : new LocalTimeValue(value));
  }

  public final PreparedStatement bindLong(final Long value) {
    return bind(value == null ? Value.NULL : new LongValue(value));
  }

  public final PreparedStatement bindOffsetTime(final OffsetTime value) {
    return bind(value == null ? Value.NULL : new OffsetTimeValue(value));
  }

  public final PreparedStatement bindShort(final Short value) {
    return bind(value == null ? Value.NULL : new ShortValue(value));
  }

  public final PreparedStatement bindString(final String value) {
    return bind(value == null ? Value.NULL : new StringValue(value));
  }

  public final PreparedStatement bindNull(final String value) {
    return bind(Value.NULL);
  }

  public final PreparedStatement bind(final Value<?> param) {
    final Value<?>[] newParams = Arrays.copyOf(params, params.length + 1);
    newParams[params.length] = param;
    return new PreparedStatement(query, newParams);
  }

  public final String getQuery() {
    return query;
  }

  public final List<Value<?>> getValues() {
    return Collections.unmodifiableList(Arrays.asList(params));
  }
}
