package io.trane.ndbc.postgres.netty4;

import io.trane.ndbc.test.DataSourceTest;

public class PostgresDataSourceTest extends DataSourceTest {

  public PostgresDataSourceTest() {
    super(PostgresConfig.instance, "varchar", "SELECT pg_sleep(999)");
  }
}
