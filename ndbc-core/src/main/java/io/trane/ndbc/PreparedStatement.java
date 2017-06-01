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

public class PreparedStatement {

  private static final Value<?>[] emptyValues = new Value<?>[0];

  public static PreparedStatement apply(String query) {
    return new PreparedStatement(query, emptyValues);
  }

  private final String query;
  private final Value<?>[] params;

  private PreparedStatement(String query, Value<?>[] params) {
    super();
    this.query = query;
    this.params = params;
  }

  public PreparedStatement bind(BigDecimal value) {
    return bind(value == null ? Value.NULL : new BigDecimalValue(value));
  }

  public PreparedStatement bind(Boolean value) {
    return bind(value == null ? Value.NULL : new BooleanValue(value));
  }

  public PreparedStatement bind(byte[] value) {
    return bind(value == null ? Value.NULL : new ByteArrayValue(value));
  }

  public PreparedStatement bind(Double value) {
    return bind(value == null ? Value.NULL : new DoubleValue(value));
  }

  public PreparedStatement bind(Float value) {
    return bind(value == null ? Value.NULL : new FloatValue(value));
  }

  public PreparedStatement bind(Integer value) {
    return bind(value == null ? Value.NULL : new IntegerValue(value));
  }

  public PreparedStatement bind(LocalDate value) {
    return bind(value == null ? Value.NULL : new LocalDateValue(value));
  }

  public PreparedStatement bind(LocalDateTime value) {
    return bind(value == null ? Value.NULL : new LocalDateTimeValue(value));
  }

  public PreparedStatement bind(LocalTime value) {
    return bind(value == null ? Value.NULL : new LocalTimeValue(value));
  }

  public PreparedStatement bind(Long value) {
    return bind(value == null ? Value.NULL : new LongValue(value));
  }

  public PreparedStatement bind(OffsetTime value) {
    return bind(value == null ? Value.NULL : new OffsetTimeValue(value));
  }

  public PreparedStatement bind(Short value) {
    return bind(value == null ? Value.NULL : new ShortValue(value));
  }

  public PreparedStatement bind(String value) {
    return bind(value == null ? Value.NULL : new StringValue(value));
  }

  public PreparedStatement bindNull(String value) {
    return bind(Value.NULL);
  }

  public PreparedStatement bind(Value<?> param) {
    Value<?>[] newParams = Arrays.copyOf(params, params.length + 1);
    newParams[params.length] = param;
    return new PreparedStatement(query, newParams);
  }

  public String getQuery() {
    return query;
  }

  public List<Value<?>> getValues() {
    return Collections.unmodifiableList(Arrays.asList(params));
  }
}
