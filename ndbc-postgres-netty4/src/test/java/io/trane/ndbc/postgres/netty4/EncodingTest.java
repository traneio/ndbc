package io.trane.ndbc.postgres.netty4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.value.Value;

public class EncodingTest extends TestEnv {

  @Test
  public void bigDecimal() throws CheckedFutureException {
    test("numeric", (ps, v) -> ps.bindBigDecimal(v), Value::getBigDecimal, r -> BigDecimal.valueOf(r.nextDouble()));
  }

  @Test
  public void _boolean() throws CheckedFutureException {
    test("bool", (ps, v) -> ps.bindBoolean(v), Value::getBoolean, Random::nextBoolean);
  }

  @Test
  public void byteArray() throws CheckedFutureException {
    test("bytea", (ps, v) -> ps.bindByteArray(v), Value::getByteArray, r -> {
      byte[] bytes = new byte[r.nextInt(5)];
      r.nextBytes(bytes);
      return bytes;
    }, (a, b) -> assertArrayEquals(a, b));
  }

  @Test
  public void double_() throws CheckedFutureException {
    test("float8", (ps, v) -> ps.bindDouble(v), Value::getDouble, Random::nextDouble);
  }

  @Test
  public void float_() throws CheckedFutureException {
    test("float4", (ps, v) -> ps.bindFloat(v), Value::getFloat, Random::nextFloat);
  }

  @Test
  public void integer_() throws CheckedFutureException {
    test("int4", (ps, v) -> ps.bindInteger(v), Value::getInteger, Random::nextInt);
  }

  @Test
  public void localDate() throws CheckedFutureException {
    test("date", (ps, v) -> ps.bindLocalDate(v), Value::getLocalDate, r -> randomLocalDateTime(r).toLocalDate());
  }

  @Test
  public void localDateTime() throws CheckedFutureException {
    test("timestamp", (ps, v) -> ps.bindLocalDateTime(v), Value::getLocalDateTime, r -> randomLocalDateTime(r));
  }

  @Test
  public void localTime() throws CheckedFutureException {
    test("time", (ps, v) -> ps.bindLocalTime(v), Value::getLocalTime, r -> randomLocalDateTime(r).toLocalTime());
  }

  @Test
  public void long_() throws CheckedFutureException {
    test("int8", (ps, v) -> ps.bindLong(v), Value::getLong, Random::nextLong);
  }

  @Test
  public void offsetTime() throws CheckedFutureException {
    test("timetz", (ps, v) -> ps.bindOffsetTime(v), Value::getOffsetTime,
        r -> randomLocalDateTime(r).toLocalTime().atOffset(randomZoneOffset(r)));
  }

  @Test
  public void short_() throws CheckedFutureException {
    test("int2", (ps, v) -> ps.bindShort(v), Value::getShort, r -> (short) r.nextInt());
  }

  private void testString(String columnType, int maxLength) throws CheckedFutureException {
    test(columnType, (ps, v) -> ps.bindString(v), Value::getString, r -> radomString(r, maxLength));
  }

  @Test
  public void string() throws CheckedFutureException {
    testString("text", 100);
    testString("name", 64);
    testString("varchar", 100);
  }

  @Test
  public void stringJson() throws CheckedFutureException {
    this.<String>test("json", (ps, v) -> ps.bindString(v), Value::getString,
        r -> "{ \"test\": " + r.nextInt(100) + " }");
  }
  
  @Test
  public void stringXml() throws CheckedFutureException {
    this.<String>test("xml", (ps, v) -> ps.bindString(v), Value::getString,
        r -> "<a/>");
  }

  private LocalDateTime randomLocalDateTime(Random r) {
    return LocalDateTime.of(r.nextInt(30000), r.nextInt(12) + 1, r.nextInt(28) + 1, r.nextInt(24), r.nextInt(60),
        r.nextInt(60), r.nextInt(99999) * 1000);
  }

  private String radomString(Random r, int maxLength) {
    int length = r.nextInt(maxLength - 1) + 1;
    StringBuilder sb = new StringBuilder();
    while (sb.length() < r.nextInt(length)) {
      char c = (char) (r.nextInt() & Character.MAX_VALUE);
      if (Character.isAlphabetic(c) || Character.isDigit(c))
        sb.append(c);
    }
    return sb.toString();
  }

  private ZoneOffset randomZoneOffset(Random r) {
    return ZoneOffset.ofTotalSeconds(r.nextInt(18 * 2) + 18);
  }

  private <T> void test(String columnType, BiFunction<PreparedStatement, T, PreparedStatement> bind,
      Function<Value<?>, T> get, Function<Random, T> gen) throws CheckedFutureException {
    test(columnType, bind, get, gen, (a, b) -> assertEquals(a, b));
  }

  private <T> void test(String columnType, BiFunction<PreparedStatement, T, PreparedStatement> bind,
      Function<Value<?>, T> get, Function<Random, T> gen, BiConsumer<T, T> verify) throws CheckedFutureException {
    test(columnType, bind, get, gen, verify, 20);
  }

  private <T> void test(String columnType, BiFunction<PreparedStatement, T, PreparedStatement> bind,
      Function<Value<?>, T> get, Function<Random, T> gen, BiConsumer<T, T> verify, int iterations)
      throws CheckedFutureException {
    String table = "test_" + columnType;
    ds.execute("DROP TABLE IF EXISTS " + table).get(timeout);
    ds.execute("CREATE TABLE " + table + " (c " + columnType + ")").get(timeout);
    
    Random r = new Random(1);
    for (int i = 0; i < iterations; i++) {
      T expected = gen.apply(r);
      try {
        ds.execute("DELETE FROM " + table).get(timeout);
        ds.execute(bind.apply(PreparedStatement.apply("INSERT INTO " + table + " VALUES (?)"), expected)).get(timeout);
        T actual = get.apply(query(PreparedStatement.apply("SELECT c FROM " + table)));
        verify.accept(expected, actual);
      } catch (Exception e) {
        throw new RuntimeException("Failure. columnType '" + columnType + "', value '" + expected + "'", e);
      }
    }
  }

  private Value<?> query(final PreparedStatement ps) throws CheckedFutureException {
    final Iterator<Row> it = ds.query(ps).get(timeout).iterator();
    assertTrue(it.hasNext());
    Row lastRow = null;
    while (it.hasNext())
      lastRow = it.next();
    return lastRow.column(0);
  }
}
