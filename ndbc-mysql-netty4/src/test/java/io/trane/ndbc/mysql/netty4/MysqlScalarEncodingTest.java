package io.trane.ndbc.mysql.netty4;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

import io.trane.ndbc.test.ScalarEncodingTest;

public class MysqlScalarEncodingTest extends ScalarEncodingTest {

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return MysqlEnv.dataSources;
  }

  protected List<String> bigDecimalColumnTypes() {
    return Arrays.asList("NUMERIC");
  }

  protected List<String> booleanColumnTypes() {
    return Arrays.asList("TINYINT");
  }

  protected List<String> byteArrayColumnTypes() {
    return Arrays.asList("BINARY");
  }

  protected List<String> doubleColumnTypes() {
    return Arrays.asList("DOUBLE");
  }

  protected List<String> floatColumnTypes() {
    return Arrays.asList("REAL");
  }

  protected List<String> integerColumnTypes() {
    return Arrays.asList("INT");
  }

  protected List<String> localDateColumnTypes() {
    return Arrays.asList("DATE");
  }

  protected List<String> localDateTimeColumnTypes() {
    return Arrays.asList("TIMESTAMP");
  }

  protected List<String> localTimeColumnTypes() {
    return Arrays.asList("TIME");
  }

  protected List<String> longColumnTypes() {
    return Arrays.asList("BIGINT");
  }

  protected List<String> offsetTimeColumnTypes() {
    return Arrays.asList("TIMETZ");
  }

  protected List<String> shortColumnTypes() {
    return Arrays.asList("SMALLINT");
  }

  protected List<String> byteColumnTypes() {
    return Arrays.asList("TINYINT");
  }

  protected List<String> stringColumnTypes() {
    return Arrays.asList("VARCHAR(9999)", "TEXT");
  }
}
