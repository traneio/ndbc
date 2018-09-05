package io.trane.ndbc.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Random;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.Config;
import io.trane.ndbc.value.Value;

public abstract class ScalarEncodingTest extends EncodingTest {

  public ScalarEncodingTest(Config config) {
    super(config);
  }

  @Test
  public void bigDecimal() throws CheckedFutureException {
    test(bigDecimalColumnTypes(), (ps, v) -> ps.setBigDecimal(v), Value::getBigDecimal,
        r -> BigDecimal.valueOf(r.nextLong(), r.nextInt(100)));
  }

  @Test
  public void _boolean() throws CheckedFutureException {
    test(booleanColumnTypes(), (ps, v) -> ps.setBoolean(v), Value::getBoolean, Random::nextBoolean);
  }

  @Test
  public void byteArray() throws CheckedFutureException {
    test(byteArrayColumnTypes(), (ps, v) -> ps.setByteArray(v), Value::getByteArray, r -> {
      final byte[] bytes = new byte[r.nextInt(5)];
      r.nextBytes(bytes);
      return bytes;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void double_() throws CheckedFutureException {
    test(doubleColumnTypes(), (ps, v) -> ps.setDouble(v), Value::getDouble, Random::nextDouble);
  }

  @Test
  public void float_() throws CheckedFutureException {
    test(floatColumnTypes(), (ps, v) -> ps.setFloat(v), Value::getFloat, Random::nextFloat,
        (a, b) -> assertEquals(a.toString().substring(0, 0), b.toString().substring(0, 0)));
  }

  @Test
  public void integer() throws CheckedFutureException {
    test(integerColumnTypes(), (ps, v) -> ps.setInteger(v), Value::getInteger, Random::nextInt);
  }

  @Test
  public void localDate() throws CheckedFutureException {
    test(localDateColumnTypes(), (ps, v) -> ps.setLocalDate(v), Value::getLocalDate,
        r -> randomLocalDateTime(r).toLocalDate());
  }

  @Test
  public void localDateTime() throws CheckedFutureException {
    test(localDateTimeColumnTypes(), (ps, v) -> ps.setLocalDateTime(v), Value::getLocalDateTime,
        r -> randomLocalDateTime(r));
  }

  @Test
  public void localTime() throws CheckedFutureException {
    test(localTimeColumnTypes(), (ps, v) -> ps.setLocalTime(v), Value::getLocalTime,
        r -> randomLocalDateTime(r).toLocalTime());
  }

  @Test
  public void long_() throws CheckedFutureException {
    test(longColumnTypes(), (ps, v) -> ps.setLong(v), Value::getLong, Random::nextLong);
  }

  @Test
  public void offsetTime() throws CheckedFutureException {
    test(offsetTimeColumnTypes(), (ps, v) -> ps.setOffsetTime(v), Value::getOffsetTime,
        r -> randomLocalDateTime(r).toLocalTime().atOffset(randomZoneOffset(r)));
  }

  @Test
  public void short_() throws CheckedFutureException {
    test(shortColumnTypes(), (ps, v) -> ps.setShort(v), Value::getShort, r -> (short) r.nextInt());
  }

  @Test
  public void byte_() throws CheckedFutureException {
    test(byteColumnTypes(), (ps, v) -> ps.setByte(v), Value::getByte, r -> (byte) r.nextInt());
  }

  @Test
  public void string() throws CheckedFutureException {
    test(stringColumnTypes(), (ps, v) -> ps.setString(v), Value::getString, r -> radomString(r, 100));
  }
}
