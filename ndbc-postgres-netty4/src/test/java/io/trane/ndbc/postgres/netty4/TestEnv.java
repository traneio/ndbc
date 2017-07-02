package io.trane.ndbc.postgres.netty4;

import java.time.Duration;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;

public class TestEnv {

  private final Config config  = Config
      .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 5432, "postgres")
      .password("postgres")
      .poolValidationInterval(Duration.ofSeconds(1))
      .poolMaxSize(10).poolMaxWaiters(10);                                                     // .ssl(SSL.apply(SSL.Mode.REQUIRE));

  protected DataSource ds      = DataSource.fromConfig(config);

  protected Duration   timeout = Duration.ofSeconds(9999);

}
