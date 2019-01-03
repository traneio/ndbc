package io.trane.ndbc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

/**
 * A prepared statement in NDBC is an immutable value and doesn't require a
 * connection or a data source to be created.
 */
public class PreparedStatement {

  private static Value<?>[] emptyValues = new Value<?>[0];

  protected final String     query;
  protected final Value<?>[] params;

  /**
   * Creates a prepared statement for a query string
   * 
   * @param query
   *          the query string
   * @return the prepared statement
   */
  public static PreparedStatement create(final String query) {
    return new PreparedStatement(query);
  }

  protected PreparedStatement(final String query) {
    this(query, emptyValues);
  }

  protected PreparedStatement(final String query, final Value<?>[] params) {
    this.query = query;
    this.params = params;
  }

  protected int nextIndex() {
    return params.length;
  }

  public PreparedStatement setBigDecimal(final BigDecimal value) {
    return setBigDecimal(nextIndex(), value);
  }

  public PreparedStatement setBigDecimal(final int index, final BigDecimal value) {
    return set(index, value == null ? Value.NULL : new BigDecimalValue(value));
  }

  public PreparedStatement setBoolean(final Boolean value) {
    return setBoolean(nextIndex(), value);
  }

  public PreparedStatement setBoolean(final int index, final Boolean value) {
    return set(index, value == null ? Value.NULL : new BooleanValue(value));
  }

  public PreparedStatement setByteArray(final byte[] value) {
    return setByteArray(nextIndex(), value);
  }

  public PreparedStatement setByteArray(final int index, final byte[] value) {
    return set(index, value == null ? Value.NULL : new ByteArrayValue(value));
  }

  public PreparedStatement setDouble(final Double value) {
    return setDouble(nextIndex(), value);
  }

  public PreparedStatement setDouble(final int index, final Double value) {
    return set(index, value == null ? Value.NULL : new DoubleValue(value));
  }

  public PreparedStatement setFloat(final Float value) {
    return setFloat(nextIndex(), value);
  }

  public PreparedStatement setFloat(final int index, final Float value) {
    return set(index, value == null ? Value.NULL : new FloatValue(value));
  }

  public PreparedStatement setInteger(final Integer value) {
    return setInteger(nextIndex(), value);
  }

  public PreparedStatement setInteger(final int index, final Integer value) {
    return set(index, value == null ? Value.NULL : new IntegerValue(value));
  }

  public PreparedStatement setLocalDate(final LocalDate value) {
    return setLocalDate(nextIndex(), value);
  }

  public PreparedStatement setLocalDate(final int index, final LocalDate value) {
    return set(index, value == null ? Value.NULL : new LocalDateValue(value));
  }

  public PreparedStatement setLocalDateTime(final LocalDateTime value) {
    return setLocalDateTime(nextIndex(), value);
  }

  public PreparedStatement setLocalDateTime(final int index, final LocalDateTime value) {
    return set(index, value == null ? Value.NULL : new LocalDateTimeValue(value));
  }

  public PreparedStatement setLocalTime(final LocalTime value) {
    return setLocalTime(nextIndex(), value);
  }

  public PreparedStatement setLocalTime(final int index, final LocalTime value) {
    return set(index, value == null ? Value.NULL : new LocalTimeValue(value));
  }

  public PreparedStatement setLong(final Long value) {
    return setLong(nextIndex(), value);
  }

  public PreparedStatement setLong(final int index, final Long value) {
    return set(index, value == null ? Value.NULL : new LongValue(value));
  }

  public PreparedStatement setByte(final Byte value) {
    return setByte(nextIndex(), value);
  }

  public PreparedStatement setByte(final int index, final Byte value) {
    return set(index, value == null ? Value.NULL : new ByteValue(value));
  }

  public PreparedStatement setShort(final Short value) {
    return setShort(nextIndex(), value);
  }

  public PreparedStatement setShort(final int index, final Short value) {
    return set(index, value == null ? Value.NULL : new ShortValue(value));
  }

  public PreparedStatement setString(final String value) {
    return setString(nextIndex(), value);
  }

  public PreparedStatement setString(final int index, final String value) {
    return set(index, value == null ? Value.NULL : new StringValue(value));
  }

  public PreparedStatement setUUID(final int index, final UUID value) {
    return set(index, value == null ? Value.NULL : new UUIDValue(value));
  }

  public PreparedStatement setUUID(final UUID value) {
    return setUUID(nextIndex(), value);
  }

  public PreparedStatement setNull() {
    return setNull(nextIndex());
  }

  public PreparedStatement setNull(final int index) {
    return set(index, Value.NULL);
  }

  public PreparedStatement set(final Value<?> param) {
    return set(nextIndex(), param);
  }

  public PreparedStatement set(final int index, final Value<?> param) {
    if (index < 0)
      throw new IllegalArgumentException("PreparedStatement binding index can't be negative");
    final Value<?>[] newParams;
    if (index >= nextIndex()) {
      newParams = Arrays.copyOf(params, index + 1);
      Arrays.fill(newParams, nextIndex(), index, Value.NULL);
    } else
      newParams = Arrays.copyOf(params, nextIndex());
    newParams[index] = param;
    return new PreparedStatement(query, newParams);
  }

  public String query() {
    return query;
  }

  public List<Value<?>> params() {
    return Collections.unmodifiableList(Arrays.asList(params));
  }

  @Override
  public String toString() {
    return "PreparedStatement [query=" + query + ", params=" + Arrays.toString(params) + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + Arrays.hashCode(params);
    result = (prime * result) + ((query == null) ? 0 : query.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    final PreparedStatement other = (PreparedStatement) obj;
    if (!Arrays.equals(params, other.params))
      return false;
    if (query == null) {
      if (other.query != null)
        return false;
    } else if (!query.equals(other.query))
      return false;
    return true;
  }

}
