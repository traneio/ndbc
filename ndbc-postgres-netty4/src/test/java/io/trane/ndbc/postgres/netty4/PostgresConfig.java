package io.trane.ndbc.postgres.netty4;

import java.time.Duration;

import io.trane.ndbc.Config;

public interface PostgresConfig {

  public static final Config instance = Config
      .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 5432, "postgres")
      .password("postgres")
      .poolValidationInterval(Duration.ofSeconds(1))
      .poolMaxSize(1)
      .poolMaxWaiters(0)
      .connectionTimeout(Duration.ofSeconds(1))
      .queryTimeout(Duration.ofSeconds(1));
  // TODO .ssl(SSL.apply(SSL.Mode.REQUIRE));
}
