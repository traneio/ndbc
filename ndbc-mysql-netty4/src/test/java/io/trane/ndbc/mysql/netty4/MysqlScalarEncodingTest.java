package io.trane.ndbc.mysql.netty4;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.runners.Parameterized.Parameters;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.test.ScalarEncodingTest;

public class MysqlScalarEncodingTest extends ScalarEncodingTest {

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return MysqlEnv.dataSources;
  }

  @Override
  protected PreparedStatement prepare(String query) {
    return PreparedStatement.apply(query);
  }

  protected List<String> bigDecimalColumnTypes() {
    return Arrays.asList("DECIMAL(20, 10)", "NUMERIC(20, 10)");
  }

  protected List<String> booleanColumnTypes() {
    return Arrays.asList("TINYINT");
  }

  protected List<String> byteArrayColumnTypes() {
    return Arrays.asList("BLOB", "TINYBLOB", "MEDIUMBLOB", "LONGBLOB");
  }

  protected List<String> doubleColumnTypes() {
    return Arrays.asList("DOUBLE");
  }

  protected List<String> floatColumnTypes() {
    return Arrays.asList("FLOAT");
  }

  protected List<String> integerColumnTypes() {
    return Arrays.asList("INT");
  }

  protected List<String> localDateColumnTypes() {
    return Arrays.asList("DATE");
  }

  protected List<String> localDateTimeColumnTypes() {
    return Arrays.asList("DATETIME(6)", "TIMESTAMP(6)");
  }

  protected List<String> localTimeColumnTypes() {
    return Arrays.asList("TIME(6)");
  }

  protected List<String> longColumnTypes() {
    return Arrays.asList("BIGINT");
  }

  protected List<String> offsetTimeColumnTypes() {
    return Arrays.asList("TIME(6)");
  }

  protected List<String> shortColumnTypes() {
    return Arrays.asList("SMALLINT");
  }

  protected List<String> byteColumnTypes() {
    return Arrays.asList("TINYINT");
  }

  protected List<String> stringColumnTypes() {
    return Arrays.asList("VARCHAR(9999)");
  }
}
