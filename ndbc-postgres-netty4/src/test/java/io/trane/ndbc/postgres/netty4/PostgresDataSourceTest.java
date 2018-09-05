package io.trane.ndbc.postgres.netty4;

import java.time.Duration;

import io.trane.ndbc.Config;
import io.trane.ndbc.test.DataSourceTest;

public class PostgresDataSourceTest extends DataSourceTest {

  private static final Config config = Config
      .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 5432, "postgres")
      .password("postgres").poolValidationInterval(Duration.ofSeconds(1)).poolMaxSize(1).poolMaxWaiters(0);
  // TODO .ssl(SSL.apply(SSL.Mode.REQUIRE));

  public PostgresDataSourceTest() {
    super(config, "varchar", "SELECT pg_sleep(999)");
  }
}
