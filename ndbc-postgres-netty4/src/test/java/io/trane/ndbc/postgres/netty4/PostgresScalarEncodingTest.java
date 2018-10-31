package io.trane.ndbc.postgres.netty4;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.test.ScalarEncodingTest;

public class PostgresScalarEncodingTest extends ScalarEncodingTest {

  @Override
  protected PreparedStatement prepare(final String query) {
    return PreparedStatement.apply(query);
  }

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return PostgresEnv.dataSources;
  }

  @Override
  protected List<String> bigDecimalColumnTypes() {
    return Arrays.asList("numeric");
  }

  @Override
  protected List<String> booleanColumnTypes() {
    return Arrays.asList("bool");
  }

  @Override
  protected List<String> byteArrayColumnTypes() {
    return Arrays.asList("bytea");
  }

  @Override
  protected List<String> doubleColumnTypes() {
    return Arrays.asList("float8");
  }

  @Override
  protected List<String> floatColumnTypes() {
    return Arrays.asList("float4");
  }

  @Override
  protected List<String> integerColumnTypes() {
    return Arrays.asList("int4");
  }

  @Override
  protected List<String> localDateColumnTypes() {
    return Arrays.asList("date");
  }

  @Override
  protected List<String> localDateTimeColumnTypes() {
    return Arrays.asList("timestamp");
  }

  @Override
  protected List<String> localTimeColumnTypes() {
    return Arrays.asList("time");
  }

  @Override
  protected List<String> longColumnTypes() {
    return Arrays.asList("int8");
  }

  protected List<String> offsetTimeColumnTypes() {
    return Arrays.asList("timetz");
  }

  @Override
  protected List<String> shortColumnTypes() {
    return Arrays.asList("int2");
  }

  @Override
  protected List<String> byteColumnTypes() {
    return Arrays.asList("smallint");
  }

  @Override
  protected List<String> stringColumnTypes() {
    return Arrays.asList("text", "name", "varchar");
  }
}
