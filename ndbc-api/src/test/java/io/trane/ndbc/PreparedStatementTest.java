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
    BigDecimal value = new BigDecimal(1.2D);
    Iterator<Value<?>> it = ps.bindBigDecimal(value).params().iterator();
    assertEquals(new BigDecimalValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindBigDecimalNull() {
    Iterator<Value<?>> it = ps.bindBigDecimal(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindBoolean() {
    Boolean value = true;
    Iterator<Value<?>> it = ps.bindBoolean(value).params().iterator();
    assertEquals(new BooleanValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindBigBoolean() {
    Iterator<Value<?>> it = ps.bindBoolean(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindByteArray() {
    byte[] value = new byte[10];
    Iterator<Value<?>> it = ps.bindByteArray(value).params().iterator();
    assertEquals(new ByteArrayValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindByteArrayNull() {
    Iterator<Value<?>> it = ps.bindByteArray(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindDouble() {
    Double value = 1.2D;
    Iterator<Value<?>> it = ps.bindDouble(value).params().iterator();
    assertEquals(new DoubleValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindDoubleNull() {
    Iterator<Value<?>> it = ps.bindDouble(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindFloat() {
    Float value = 1.2F;
    Iterator<Value<?>> it = ps.bindFloat(value).params().iterator();
    assertEquals(new FloatValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindFloatNull() {
    Iterator<Value<?>> it = ps.bindFloat(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindInteger() {
    Integer value = 33;
    Iterator<Value<?>> it = ps.bindInteger(value).params().iterator();
    assertEquals(new IntegerValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindIntegerNull() {
    Iterator<Value<?>> it = ps.bindInteger(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalDate() {
    LocalDate value = LocalDate.now();
    Iterator<Value<?>> it = ps.bindLocalDate(value).params().iterator();
    assertEquals(new LocalDateValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalDateNull() {
    Iterator<Value<?>> it = ps.bindLocalDate(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalDateTime() {
    LocalDateTime value = LocalDateTime.now();
    Iterator<Value<?>> it = ps.bindLocalDateTime(value).params().iterator();
    assertEquals(new LocalDateTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalDateTimeNull() {
    Iterator<Value<?>> it = ps.bindLocalDateTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalTime() {
    LocalTime value = LocalTime.now();
    Iterator<Value<?>> it = ps.bindLocalTime(value).params().iterator();
    assertEquals(new LocalTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLocalTimeNull() {
    Iterator<Value<?>> it = ps.bindLocalTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLong() {
    Long value = 212L;
    Iterator<Value<?>> it = ps.bindLong(value).params().iterator();
    assertEquals(new LongValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindLongNull() {
    Iterator<Value<?>> it = ps.bindLong(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindOffsetTime() {
    OffsetTime value = OffsetTime.now();
    Iterator<Value<?>> it = ps.bindOffsetTime(value).params().iterator();
    assertEquals(new OffsetTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindOffsetTimeNull() {
    Iterator<Value<?>> it = ps.bindOffsetTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindShort() {
    Short value = 31;
    Iterator<Value<?>> it = ps.bindShort(value).params().iterator();
    assertEquals(new ShortValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindShortNull() {
    Iterator<Value<?>> it = ps.bindShort(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindString() {
    String value = "s";
    Iterator<Value<?>> it = ps.bindString(value).params().iterator();
    assertEquals(new StringValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindStringNull() {
    Iterator<Value<?>> it = ps.bindString(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void multipleBindings() {
    Integer v1 = 11;
    Short v2 = 2;
    Float v3 = 2.3F;

    PreparedStatement ps = this.ps;
    ps = ps.bindInteger(v1);
    ps = ps.bindShort(v2);
    ps = ps.bindFloat(v3);

    Iterator<Value<?>> it = ps.params().iterator();
    assertEquals(new IntegerValue(v1), it.next());
    assertEquals(new ShortValue(v2), it.next());
    assertEquals(new FloatValue(v3), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bind() {
    IntegerValue value = new IntegerValue(11);
    Iterator<Value<?>> it = ps.bind(value).params().iterator();
    assertEquals(value, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void bindNull() {
    Iterator<Value<?>> it = ps.bindNull().params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }
}
