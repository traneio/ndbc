package io.trane.ndbc.sqlserver.netty4;

import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import io.trane.ndbc.test.DataSourceTest;

public class SqlServerDataSourceTest extends DataSourceTest {

  public SqlServerDataSourceTest() {
    super("varchar", "WAITFOR DELAY '00:16'");
  }

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return SqlServerEnv.dataSources;
  }
}
