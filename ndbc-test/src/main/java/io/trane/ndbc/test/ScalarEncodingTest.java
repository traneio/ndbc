package io.trane.ndbc.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Random;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public abstract class ScalarEncodingTest extends EncodingTest<PreparedStatement, Row> {

  @Test
  public void bigDecimal() throws CheckedFutureException {
    test(bigDecimalColumnTypes(), (ps, v) -> ps.setBigDecimal(v), Row::getBigDecimal,
        r -> BigDecimal.valueOf(r.nextInt(5), r.nextInt(5)), (a, b) -> assertEquals(a.compareTo(b), 0));
  }

  @Test
  public void _boolean() throws CheckedFutureException {
    test(booleanColumnTypes(), (ps, v) -> ps.setBoolean(v), Row::getBoolean, Random::nextBoolean);
  }

  @Test
  public void byteArray() throws CheckedFutureException {
    test(byteArrayColumnTypes(), (ps, v) -> ps.setByteArray(v), Row::getByteArray, r -> {
      // final byte[] bytes = new byte[r.nextInt(5)];
      // r.nextBytes(bytes);
      // return bytes;
      return new byte[] { 111, 14, -5 };
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void double_() throws CheckedFutureException {
    test(doubleColumnTypes(), (ps, v) -> ps.setDouble(v), Row::getDouble, Random::nextDouble);
  }

  @Test
  public void float_() throws CheckedFutureException {
    test(floatColumnTypes(), (ps, v) -> ps.setFloat(v), Row::getFloat, Random::nextFloat,
        (a, b) -> assertEquals(a.floatValue(), b.floatValue(), 0.001f));
  }

  @Test
  public void integer() throws CheckedFutureException {
    test(integerColumnTypes(), (ps, v) -> ps.setInteger(v), Row::getInteger, Random::nextInt);
  }

  @Test
  public void localDate() throws CheckedFutureException {
    test(localDateColumnTypes(), (ps, v) -> ps.setLocalDate(v), Row::getLocalDate,
        r -> randomLocalDateTime(r).toLocalDate());
  }

  @Test
  public void localDateTime() throws CheckedFutureException {
    test(localDateTimeColumnTypes(), (ps, v) -> ps.setLocalDateTime(v), Row::getLocalDateTime,
        r -> randomLocalDateTime(r));
  }

  @Test
  public void localTime() throws CheckedFutureException {
    test(localTimeColumnTypes(), (ps, v) -> ps.setLocalTime(v), Row::getLocalTime,
        r -> randomLocalDateTime(r).toLocalTime());
  }

  @Test
  public void long_() throws CheckedFutureException {
    test(longColumnTypes(), (ps, v) -> ps.setLong(v), Row::getLong, Random::nextLong);
  }

  @Test
  public void short_() throws CheckedFutureException {
    test(shortColumnTypes(), (ps, v) -> ps.setShort(v), Row::getShort, r -> (short) r.nextInt());
  }

  @Test
  public void byte_() throws CheckedFutureException {
    test(byteColumnTypes(), (ps, v) -> ps.setByte(v), Row::getByte, r -> (byte) r.nextInt());
  }

  @Test
  public void string() throws CheckedFutureException {
    test(stringColumnTypes(), (ps, v) -> ps.setString(v), Row::getString, r -> radomString(r, 100));
  }
}
