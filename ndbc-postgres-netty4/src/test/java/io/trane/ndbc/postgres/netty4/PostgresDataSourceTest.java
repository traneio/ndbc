package io.trane.ndbc.postgres.netty4;

import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import io.trane.ndbc.test.DataSourceTest;

public class PostgresDataSourceTest extends DataSourceTest {

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return PostgresEnv.dataSources;
  }

  public PostgresDataSourceTest() {
    super("varchar", "SELECT pg_sleep(999)");
  }
}
