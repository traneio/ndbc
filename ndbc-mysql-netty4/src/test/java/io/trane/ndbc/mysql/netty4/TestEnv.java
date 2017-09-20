package io.trane.ndbc.mysql.netty4;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;

import java.time.Duration;

public class TestEnv {

  private final Config config  = Config
          .apply("io.trane.ndbc.mysql.netty4.DataSourceSupplier", "localhost", 3306, "root")
          .password("mysql")
          .database("mysql")
          .poolValidationInterval(Duration.ofSeconds(1))
          .poolMaxSize(1).poolMaxWaiters(0);

  protected DataSource ds      = DataSource.fromConfig(config);

  protected Duration   timeout = Duration.ofSeconds(999);
}
