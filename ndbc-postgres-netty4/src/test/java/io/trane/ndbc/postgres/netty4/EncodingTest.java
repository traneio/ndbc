package io.trane.ndbc.postgres.netty4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Ignore;
import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.value.Value;

public class EncodingTest extends TestEnv {

  @Test
  public void bigDecimal() throws CheckedFutureException {
    test("numeric", (ps, v) -> ps.setBigDecimal(v), Value::getBigDecimal,
        r -> BigDecimal.valueOf(r.nextLong(), r.nextInt(100)));
  }

  @Test
  @Ignore
  public void bigDecimalArray() throws CheckedFutureException {
    test("numeric[]", (ps, v) -> ps.setBigDecimalArray(v), Value::getBigDecimalArray, r -> {
      final BigDecimal[] array = new BigDecimal[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = BigDecimal.valueOf(r.nextLong(), r.nextInt(100));
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void _boolean() throws CheckedFutureException {
    test("bool", (ps, v) -> ps.setBoolean(v), Value::getBoolean, Random::nextBoolean);
  }

  @Test
  @Ignore
  public void booleanArray() throws CheckedFutureException {
    test("bool[]", (ps, v) -> ps.setBooleanArray(v), Value::getBooleanArray, r -> {
      final Boolean[] array = new Boolean[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextBoolean();
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void byteArray() throws CheckedFutureException {
    test("bytea", (ps, v) -> ps.setByteArray(v), Value::getByteArray, r -> {
      final byte[] bytes = new byte[r.nextInt(5)];
      r.nextBytes(bytes);
      return bytes;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  @Ignore
  public void booleanArrayArray() throws CheckedFutureException {
    test("bytea[]", (ps, v) -> ps.setByteArrayArray(v), Value::getByteArrayArray, r -> {
      final byte[][] array = new byte[r.nextInt(10)][];
      for (int i = 0; i < array.length; i++) {
        final byte[] bytes = new byte[r.nextInt(5)];
        r.nextBytes(bytes);
        array[i] = bytes;
      }
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void double_() throws CheckedFutureException {
    test("float8", (ps, v) -> ps.setDouble(v), Value::getDouble, Random::nextDouble);
  }

  @Test
  @Ignore
  public void doubleArray() throws CheckedFutureException {
    test("float8[]", (ps, v) -> ps.setDoubleArray(v), Value::getDoubleArray, r -> {
      final Double[] array = new Double[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextDouble();
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void float_() throws CheckedFutureException {
    test("float4", (ps, v) -> ps.setFloat(v), Value::getFloat, Random::nextFloat,
        (a, b) -> assertEquals(a.toString().substring(0, 0), b.toString().substring(0, 0)));
  }

  @Test
  @Ignore
  public void floatArray() throws CheckedFutureException {
    test("float4[]", (ps, v) -> ps.setFloatArray(v), Value::getFloatArray, r -> {
      final Float[] array = new Float[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextFloat();
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void integer() throws CheckedFutureException {
    test("int4", (ps, v) -> ps.setInteger(v), Value::getInteger, Random::nextInt);
  }

  @Test
  @Ignore
  public void integerArray() throws CheckedFutureException {
    test("int4[]", (ps, v) -> ps.setIntegerArray(v), Value::getIntegerArray, r -> {
      final Integer[] array = new Integer[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextInt();
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void localDate() throws CheckedFutureException {
    test("date", (ps, v) -> ps.setLocalDate(v), Value::getLocalDate, r -> randomLocalDateTime(r).toLocalDate());
  }

  @Test
  @Ignore
  public void localDateArray() throws CheckedFutureException {
    test("date[]", (ps, v) -> ps.setLocalDateArray(v), Value::getLocalDateArray, r -> {
      final LocalDate[] array = new LocalDate[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = randomLocalDateTime(r).toLocalDate();
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void localDateTime() throws CheckedFutureException {
    test("timestamp", (ps, v) -> ps.setLocalDateTime(v), Value::getLocalDateTime, r -> randomLocalDateTime(r));
  }

  @Test
  @Ignore
  public void localDateTimeArray() throws CheckedFutureException {
    test("timestamp[]", (ps, v) -> ps.setLocalDateTimeArray(v), Value::getLocalDateTimeArray, r -> {
      final LocalDateTime[] array = new LocalDateTime[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = randomLocalDateTime(r);
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void localTime() throws CheckedFutureException {
    test("time", (ps, v) -> ps.setLocalTime(v), Value::getLocalTime, r -> randomLocalDateTime(r).toLocalTime());
  }

  @Test
  @Ignore
  public void localTimeArray() throws CheckedFutureException {
    test("time[]", (ps, v) -> ps.setLocalTimeArray(v), Value::getLocalTimeArray, r -> {
      final LocalTime[] array = new LocalTime[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = randomLocalDateTime(r).toLocalTime();
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void long_() throws CheckedFutureException {
    test("int8", (ps, v) -> ps.setLong(v), Value::getLong, Random::nextLong);
  }

  @Test
  @Ignore
  public void longArray() throws CheckedFutureException {
    test("int8[]", (ps, v) -> ps.setLongArray(v), Value::getLongArray, r -> {
      final Long[] array = new Long[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = r.nextLong();
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void offsetTime() throws CheckedFutureException {
    test("timetz", (ps, v) -> ps.setOffsetTime(v), Value::getOffsetTime,
        r -> randomLocalDateTime(r).toLocalTime().atOffset(randomZoneOffset(r)));
  }

  @Test
  @Ignore
  public void offsetTimeArray() throws CheckedFutureException {
    test("timetz[]", (ps, v) -> ps.setOffsetTimeArray(v), Value::getOffsetTimeArray, r -> {
      final OffsetTime[] array = new OffsetTime[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = randomLocalDateTime(r).toLocalTime().atOffset(randomZoneOffset(r));
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void short_() throws CheckedFutureException {
    test("int2", (ps, v) -> ps.setShort(v), Value::getShort, r -> (short) r.nextInt());
  }

  @Test
  @Ignore
  public void shortArray() throws CheckedFutureException {
    test("int2[]", (ps, v) -> ps.setShortArray(v), Value::getShortArray, r -> {
      final Short[] array = new Short[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = (short) r.nextInt();
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void byte_() throws CheckedFutureException {
    test("smallint", (ps, v) -> ps.setByte(v), Value::getByte, r -> (byte) r.nextInt());
  }

  private void testString(final String columnType, final int maxLength) throws CheckedFutureException {
    test(columnType, (ps, v) -> ps.setString(v), Value::getString, r -> radomString(r, maxLength));
  }

  @Test
  public void string() throws CheckedFutureException {
    testString("text", 100);
    testString("name", 64);
    testString("varchar", 100);
  }

  private void testStringArray(final String columnType, final int maxLength) throws CheckedFutureException {
    test(columnType, (ps, v) -> ps.setStringArray(v), Value::getStringArray, r -> {
      final String[] array = new String[r.nextInt(10)];
      for (int i = 0; i < array.length; i++)
        array[i] = radomString(r, maxLength);
      return array;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  @Ignore
  public void stringArray() throws CheckedFutureException {
    testStringArray("text[]", 100);
    testStringArray("name[]", 64);
    testStringArray("varchar[]", 100);
  }

  private LocalDateTime randomLocalDateTime(final Random r) {
    return LocalDateTime.of(r.nextInt(5000 - 1971) + 1971, r.nextInt(12) + 1, r.nextInt(28) + 1, r.nextInt(24),
        r.nextInt(60), r.nextInt(60), r.nextInt(99999) * 1000);
  }

  private String radomString(final Random r, final int maxLength) {
    final int length = r.nextInt(maxLength - 1) + 1;
    final StringBuilder sb = new StringBuilder();
    while (sb.length() < r.nextInt(length)) {
      final char c = (char) (r.nextInt() & Character.MAX_VALUE);
      if (Character.isAlphabetic(c) || Character.isDigit(c))
        sb.append(c);
    }
    return sb.toString();
  }

  private ZoneOffset randomZoneOffset(final Random r) {
    return ZoneOffset.ofTotalSeconds(r.nextInt(18 * 2) + 18);
  }

  private <T> void test(final String columnType, final BiFunction<PreparedStatement, T, PreparedStatement> set,
      final Function<Value<?>, T> get, final Function<Random, T> gen) throws CheckedFutureException {
    test(columnType, set, get, gen, (a, b) -> assertEquals(a, b));
  }

  private <T> void test(final String columnType, final BiFunction<PreparedStatement, T, PreparedStatement> set,
      final Function<Value<?>, T> get, final Function<Random, T> gen, final BiConsumer<T, T> verify)
      throws CheckedFutureException {
    test(columnType, set, get, gen, verify, 20);
  }

  private static AtomicInteger tableSuffix = new AtomicInteger(0);

  private <T> void test(final String columnType, final BiFunction<PreparedStatement, T, PreparedStatement> set,
      final Function<Value<?>, T> get, final Function<Random, T> gen, final BiConsumer<T, T> verify,
      final int iterations) throws CheckedFutureException {
    final String table = "test_encoding_" + tableSuffix.incrementAndGet();
    ds.execute("DROP TABLE IF EXISTS " + table).get(timeout);
    ds.execute("CREATE TABLE " + table + " (c " + columnType + ")").get(timeout);

    final Random r = new Random(1);
    for (int i = 0; i < iterations; i++) {
      final T expected = gen.apply(r);
      try {
        ds.execute("DELETE FROM " + table).get(timeout);
        ds.execute(set.apply(PreparedStatement.apply("INSERT INTO " + table + " VALUES (?)"), expected))
            .get(timeout);

        final T simpleQueryActual = get.apply(query("SELECT c FROM " + table));
        verify.accept(expected, simpleQueryActual);

        final T extendedQueryactual = get.apply(query(PreparedStatement.apply("SELECT c FROM " + table)));
        verify.accept(expected, extendedQueryactual);

      } catch (final Exception e) {
        String s;
        if (expected.getClass().isArray())
          s = Arrays.toString((Object[]) expected);
        else
          s = expected.toString();
        throw new RuntimeException("Failure. columnType '" + columnType + "', value '" + s + "'", e);
      }
    }
  }

  private final Value<?> query(final String query) throws CheckedFutureException {
    return lastRow(ds.query(query).get(timeout).iterator());
  }

  private final Value<?> query(final PreparedStatement ps) throws CheckedFutureException {
    return lastRow(ds.query(ps).get(timeout).iterator());
  }

  private final Value<?> lastRow(Iterator<Row> it) {
    assertTrue(it.hasNext());
    Row lastRow = null;
    while (it.hasNext())
      lastRow = it.next();
    return lastRow.column(0);
  }
}
