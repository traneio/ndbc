package io.trane.ndbc.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.value.Value;

public abstract class EncodingTest extends NdbcTest {

  protected Duration timeout = Duration.ofSeconds(999);

  protected abstract List<String> bigDecimalColumnTypes();

  protected abstract List<String> booleanColumnTypes();

  protected abstract List<String> byteArrayColumnTypes();

  protected abstract List<String> doubleColumnTypes();

  protected abstract List<String> floatColumnTypes();

  protected abstract List<String> integerColumnTypes();

  protected abstract List<String> localDateColumnTypes();

  protected abstract List<String> localDateTimeColumnTypes();

  protected abstract List<String> localTimeColumnTypes();

  protected abstract List<String> longColumnTypes();

  protected abstract List<String> offsetTimeColumnTypes();

  protected abstract List<String> shortColumnTypes();

  protected abstract List<String> byteColumnTypes();

  protected abstract List<String> stringColumnTypes();

  protected LocalDateTime randomLocalDateTime(final Random r) {
    return LocalDateTime.of(r.nextInt(60) + 1971, r.nextInt(12) + 1, r.nextInt(28) + 1, r.nextInt(24),
        r.nextInt(60), r.nextInt(60), r.nextInt(99999) * 1000);
  }

  protected String radomString(final Random r, final int maxLength) {
    final int length = r.nextInt(maxLength - 1) + 1;
    final StringBuilder sb = new StringBuilder();
    while (sb.length() < r.nextInt(length)) {
      final char c = (char) (r.nextInt() & Character.MAX_VALUE);
      if (Character.isAlphabetic(c) || Character.isDigit(c))
        sb.append(c);
    }
    return sb.toString();
  }

  protected ZoneOffset randomZoneOffset(final Random r) {
    return ZoneOffset.ofTotalSeconds(r.nextInt(18 * 2) + 18);
  }

  protected <T> void test(final List<String> columnTypes, final BiFunction<PreparedStatement, T, PreparedStatement> set,
      final Function<Value<?>, T> get, final Function<Random, T> gen) throws CheckedFutureException {
    test(columnTypes, set, get, gen, (a, b) -> assertEquals(a, b));
  }

  protected <T> void testArray(final List<String> columnTypes,
      final BiFunction<PreparedStatement, T[], PreparedStatement> set, final Function<Value<?>, T[]> get,
      final Function<Random, T[]> gen) throws CheckedFutureException {
    test(columnTypes, set, get, gen, (a, b) -> assertArrayEquals(a, b));
  }

  protected <T> void test(final List<String> columnTypes, final BiFunction<PreparedStatement, T, PreparedStatement> set,
      final Function<Value<?>, T> get, final Function<Random, T> gen, final BiConsumer<T, T> verify)
      throws CheckedFutureException {
    test(columnTypes, set, get, gen, verify, 20);
  }

  private static AtomicInteger tableSuffix = new AtomicInteger(0);

  protected <T> void test(final List<String> columnTypes, final BiFunction<PreparedStatement, T, PreparedStatement> set,
      final Function<Value<?>, T> get, final Function<Random, T> gen, final BiConsumer<T, T> verify,
      final int iterations) throws CheckedFutureException {

    for (final String columnType : columnTypes) {
      final String table = "test_encoding_" + tableSuffix.incrementAndGet();
      ds.execute("DROP TABLE IF EXISTS " + table).get(timeout);
      ds.execute("CREATE TABLE " + table + " (c " + columnType + ")").get(timeout);

      final List<String> previousValues = new ArrayList<String>();
      final Random r = new Random(1);
      for (int i = 0; i < iterations; i++) {
        final T expected = gen.apply(r);
        ds.execute("DELETE FROM " + table).get(timeout);

        String failureMessage = " Failure: columnType '" + columnType + "', value '"
            + PrettyPrint.apply(expected) + "'. Previous successful values: "
            + previousValues.stream().map(Object::toString).collect(Collectors.joining(", "));

        try {
          ds.execute(set.apply(PreparedStatement.apply("INSERT INTO " + table + " VALUES (?)"), expected)).get(timeout);
        } catch (final Throwable e) {
          throw new RuntimeException("(extended insert)" + failureMessage, e);
        }

        try {
          final T simpleQueryActual = get.apply(query("SELECT c FROM " +
              table));
          verify.accept(expected, simpleQueryActual);

        } catch (final Throwable e) {
          throw new RuntimeException("(simple query)" + failureMessage, e);
        }

        try {
          final T extendedQueryactual = get.apply(query(PreparedStatement.apply("SELECT c FROM " + table)));
          verify.accept(expected, extendedQueryactual);

        } catch (final Throwable e) {
          throw new RuntimeException("(extended query)" + failureMessage, e);
        }

        previousValues.add(PrettyPrint.apply(expected));
      }
    }
  }

  protected final Value<?> query(final String query) throws CheckedFutureException {
    return lastRow(ds.query(query).get(timeout).iterator());
  }

  protected final Value<?> query(final PreparedStatement ps) throws CheckedFutureException {
    return lastRow(ds.query(ps).get(timeout).iterator());
  }

  private final Value<?> lastRow(final Iterator<Row> it) {
    assertTrue(it.hasNext());
    Row lastRow = null;
    while (it.hasNext())
      lastRow = it.next();
    return lastRow.column(0);
  }
}
