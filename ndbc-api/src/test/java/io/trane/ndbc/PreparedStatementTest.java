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
  public void setBigDecimal() {
    final BigDecimal value = new BigDecimal(1.2D);
    final Iterator<Value<?>> it = ps.setBigDecimal(value).params().iterator();
    assertEquals(new BigDecimalValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setBigDecimalNull() {
    final Iterator<Value<?>> it = ps.setBigDecimal(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setBoolean() {
    final Boolean value = true;
    final Iterator<Value<?>> it = ps.setBoolean(value).params().iterator();
    assertEquals(new BooleanValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setBigBoolean() {
    final Iterator<Value<?>> it = ps.setBoolean(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setByteArray() {
    final byte[] value = new byte[10];
    final Iterator<Value<?>> it = ps.setByteArray(value).params().iterator();
    assertEquals(new ByteArrayValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setByteArrayNull() {
    final Iterator<Value<?>> it = ps.setByteArray(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setDouble() {
    final Double value = 1.2D;
    final Iterator<Value<?>> it = ps.setDouble(value).params().iterator();
    assertEquals(new DoubleValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setDoubleNull() {
    final Iterator<Value<?>> it = ps.setDouble(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setFloat() {
    final Float value = 1.2F;
    final Iterator<Value<?>> it = ps.setFloat(value).params().iterator();
    assertEquals(new FloatValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setFloatNull() {
    final Iterator<Value<?>> it = ps.setFloat(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setInteger() {
    final Integer value = 33;
    final Iterator<Value<?>> it = ps.setInteger(value).params().iterator();
    assertEquals(new IntegerValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setIntegerNull() {
    final Iterator<Value<?>> it = ps.setInteger(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setLocalDate() {
    final LocalDate value = LocalDate.now();
    final Iterator<Value<?>> it = ps.setLocalDate(value).params().iterator();
    assertEquals(new LocalDateValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setLocalDateNull() {
    final Iterator<Value<?>> it = ps.setLocalDate(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setLocalDateTime() {
    final LocalDateTime value = LocalDateTime.now();
    final Iterator<Value<?>> it = ps.setLocalDateTime(value).params().iterator();
    assertEquals(new LocalDateTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setLocalDateTimeNull() {
    final Iterator<Value<?>> it = ps.setLocalDateTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setLocalTime() {
    final LocalTime value = LocalTime.now();
    final Iterator<Value<?>> it = ps.setLocalTime(value).params().iterator();
    assertEquals(new LocalTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setLocalTimeNull() {
    final Iterator<Value<?>> it = ps.setLocalTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setLong() {
    final Long value = 212L;
    final Iterator<Value<?>> it = ps.setLong(value).params().iterator();
    assertEquals(new LongValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setLongNull() {
    final Iterator<Value<?>> it = ps.setLong(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setOffsetTime() {
    final OffsetTime value = OffsetTime.now();
    final Iterator<Value<?>> it = ps.setOffsetTime(value).params().iterator();
    assertEquals(new OffsetTimeValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setOffsetTimeNull() {
    final Iterator<Value<?>> it = ps.setOffsetTime(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setShort() {
    final Short value = 31;
    final Iterator<Value<?>> it = ps.setShort(value).params().iterator();
    assertEquals(new ShortValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setShortNull() {
    final Iterator<Value<?>> it = ps.setShort(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setString() {
    final String value = "s";
    final Iterator<Value<?>> it = ps.setString(value).params().iterator();
    assertEquals(new StringValue(value), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setStringNull() {
    final Iterator<Value<?>> it = ps.setString(null).params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void multipleBindings() {
    final Integer v1 = 11;
    final Short v2 = 2;
    final Float v3 = 2.3F;

    PreparedStatement ps = this.ps;
    ps = ps.setInteger(v1);
    ps = ps.setShort(v2);
    ps = ps.setFloat(v3);

    final Iterator<Value<?>> it = ps.params().iterator();
    assertEquals(new IntegerValue(v1), it.next());
    assertEquals(new ShortValue(v2), it.next());
    assertEquals(new FloatValue(v3), it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void set() {
    final IntegerValue value = new IntegerValue(11);
    final Iterator<Value<?>> it = ps.set(value).params().iterator();
    assertEquals(value, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  public void setNull() {
    final Iterator<Value<?>> it = ps.setNull().params().iterator();
    assertEquals(Value.NULL, it.next());
    assertFalse(it.hasNext());
  }
}
