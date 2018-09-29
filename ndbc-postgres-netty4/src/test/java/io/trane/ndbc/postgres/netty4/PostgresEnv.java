package io.trane.ndbc.postgres.netty4;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;

public class PostgresEnv {

  private static final Config config(String version) {
    return Config
        .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 0, "test")
        .embedded(Config.Embedded.apply("io.trane.ndbc.postgres.embedded.EmbeddedSupplier", Optional.of(version)))
        .database("test_schema")
        .password("test")
        .poolValidationInterval(Duration.ofSeconds(1));
  }

  private static final List<String> versions;

  static {
    if (System.getenv("TRAVIS_BRANCH") != null)
      versions = Arrays.asList(
          "V9_5",
          "V9_6");
    else
      versions = Arrays.asList("V9_6");
  }

  public static final List<Object[]> dataSources = versions.stream()
      .map(v -> new Object[] { DataSource.fromConfig(config(v)), "PG " + v })
      .collect(Collectors.toList());
}
