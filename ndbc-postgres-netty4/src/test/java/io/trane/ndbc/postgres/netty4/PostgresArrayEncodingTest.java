package io.trane.ndbc.postgres.netty4;

import static org.junit.Assert.assertArrayEquals;

import java.time.OffsetTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.test.ArrayEncodingTest;
import io.trane.ndbc.value.OffsetTimeArrayValue;

public class PostgresArrayEncodingTest extends ArrayEncodingTest {

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return PostgresEnv.dataSources;
  }

  @Test
  public void offsetTimeArray() throws CheckedFutureException {
    this.<OffsetTimeArrayValue>test(offsetTimeColumnTypes(), PreparedStatement::set, v -> (OffsetTimeArrayValue) v,
        r -> {
          final OffsetTime[] array = new OffsetTime[r.nextInt(10)];
          for (int i = 0; i < array.length; i++)
            array[i] = randomLocalDateTime(r).toLocalTime().atOffset(randomZoneOffset(r));
          return new OffsetTimeArrayValue(array);
        }, (a, b) -> assertArrayEquals(a.get(), b.get()));
  }

  protected List<String> bigDecimalColumnTypes() {
    return Arrays.asList("numeric[]");
  }

  protected List<String> bigIntegerColumnTypes() {
    return Arrays.asList("bigint[]");
  }

  protected List<String> booleanColumnTypes() {
    return Arrays.asList("bool[]");
  }

  protected List<String> byteArrayColumnTypes() {
    return Arrays.asList("bytea[]");
  }

  protected List<String> doubleColumnTypes() {
    return Arrays.asList("float8[]");
  }

  protected List<String> floatColumnTypes() {
    return Arrays.asList("float4[]");
  }

  protected List<String> integerColumnTypes() {
    return Arrays.asList("int4[]");
  }

  protected List<String> localDateColumnTypes() {
    return Arrays.asList("date[]");
  }

  protected List<String> localDateTimeColumnTypes() {
    return Arrays.asList("timestamp[]");
  }

  protected List<String> localTimeColumnTypes() {
    return Arrays.asList("time[]");
  }

  protected List<String> longColumnTypes() {
    return Arrays.asList("int8[]");
  }

  protected List<String> offsetTimeColumnTypes() {
    return Arrays.asList("timetz[]");
  }

  protected List<String> shortColumnTypes() {
    return Arrays.asList("int2[]");
  }

  protected List<String> byteColumnTypes() {
    return Arrays.asList("smallint[]");
  }

  protected List<String> stringColumnTypes() {
    return Arrays.asList("text[]", "varchar[]");
  }
}
