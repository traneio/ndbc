package io.trane.ndbc.mysql.netty4;

import java.util.Collection;

import org.junit.runners.Parameterized.Parameters;

import io.trane.ndbc.test.DataSourceTest;

public class MysqlDataSourceTest extends DataSourceTest {

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return MysqlEnv.dataSources;
  }

  public MysqlDataSourceTest() {
    super("varchar(20)", "SELECT SLEEP(999)");
  }
}
