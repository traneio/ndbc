package io.trane.ndbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.util.Iterator;

import org.junit.Test;

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

public class PreparedStatementTest {

  String            query = "SELECT 1";
  PreparedStatement ps    = PreparedStatement.apply(query);

  @Test
  public void apply() {
    assertEquals(query, ps.query());
  }

  @Test
  public void bindBigDecimal() {
    final BigDecimal value = new BigDecimal(1.2D);
    final Iterator<Value<?>> it = ps.bindBigDecimal(value).params().iterator();
    assertEquals(new BigDecimalValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindBigDecimalNull() {
    final Iterator<Value<?>> it = ps.bindBigDecimal(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindBoolean() {
    final Boolean value = true;
    final Iterator<Value<?>> it = ps.bindBoolean(value).params().iterator();
    assertEquals(new BooleanValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindBigBoolean() {
    final Iterator<Value<?>> it = ps.bindBoolean(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindByteArray() {
    final byte[] value = new byte[10];
    final Iterator<Value<?>> it = ps.bindByteArray(value).params().iterator();
    assertEquals(new ByteArrayValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindByteArrayNull() {
    final Iterator<Value<?>> it = ps.bindByteArray(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindDouble() {
    final Double value = 1.2D;
    final Iterator<Value<?>> it = ps.bindDouble(value).params().iterator();
    assertEquals(new DoubleValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindDoubleNull() {
    final Iterator<Value<?>> it = ps.bindDouble(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindFloat() {
    final Float value = 1.2F;
    final Iterator<Value<?>> it = ps.bindFloat(value).params().iterator();
    assertEquals(new FloatValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindFloatNull() {
    final Iterator<Value<?>> it = ps.bindFloat(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindInteger() {
    final Integer value = 33;
    final Iterator<Value<?>> it = ps.bindInteger(value).params().iterator();
    assertEquals(new IntegerValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindIntegerNull() {
    final Iterator<Value<?>> it = ps.bindInteger(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalDate() {
    final LocalDate value = LocalDate.now();
    final Iterator<Value<?>> it = ps.bindLocalDate(value).params().iterator();
    assertEquals(new LocalDateValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalDateNull() {
    final Iterator<Value<?>> it = ps.bindLocalDate(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalDateTime() {
    final LocalDateTime value = LocalDateTime.now();
    final Iterator<Value<?>> it = ps.bindLocalDateTime(value).params().iterator();
    assertEquals(new LocalDateTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalDateTimeNull() {
    final Iterator<Value<?>> it = ps.bindLocalDateTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalTime() {
    final LocalTime value = LocalTime.now();
    final Iterator<Value<?>> it = ps.bindLocalTime(value).params().iterator();
    assertEquals(new LocalTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalTimeNull() {
    final Iterator<Value<?>> it = ps.bindLocalTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLong() {
    final Long value = 212L;
    final Iterator<Value<?>> it = ps.bindLong(value).params().iterator();
    assertEquals(new LongValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLongNull() {
    final Iterator<Value<?>> it = ps.bindLong(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindOffsetTime() {
    final OffsetTime value = OffsetTime.now();
    final Iterator<Value<?>> it = ps.bindOffsetTime(value).params().iterator();
    assertEquals(new OffsetTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindOffsetTimeNull() {
    final Iterator<Value<?>> it = ps.bindOffsetTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindShort() {
    final Short value = 31;
    final Iterator<Value<?>> it = ps.bindShort(value).params().iterator();
    assertEquals(new ShortValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindShortNull() {
    final Iterator<Value<?>> it = ps.bindShort(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindString() {
    final String value = "s";
    final Iterator<Value<?>> it = ps.bindString(value).params().iterator();
    assertEquals(new StringValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindStringNull() {
    final Iterator<Value<?>> it = ps.bindString(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void multipleBindings() {
    final Integer v1 = 11;
    final Short v2 = 2;
    final Float v3 = 2.3F;

    PreparedStatement ps = this.ps;
    ps = ps.bindInteger(v1);
    ps = ps.bindShort(v2);
    ps = ps.bindFloat(v3);

    final Iterator<Value<?>> it = ps.params().iterator();
    assertEquals(new IntegerValue(v1), it.next());
    assertEquals(new ShortValue(v2), it.next());
    assertEquals(new FloatValue(v3), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bind() {
    final IntegerValue value = new IntegerValue(11);
    final Iterator<Value<?>> it = ps.bind(value).params().iterator();
    assertEquals(value, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindNull() {
    final Iterator<Value<?>> it = ps.bindNull().params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }
}
