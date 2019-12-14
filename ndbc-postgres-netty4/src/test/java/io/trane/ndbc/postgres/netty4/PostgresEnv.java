package io.trane.ndbc.postgres.netty4;

import static java.util.Arrays.asList;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;

public class PostgresEnv {

  private static final Config config(final String version) {
    return Config
        .create("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 0, "test")
        .embedded("io.trane.ndbc.postgres.embedded.EmbeddedSupplier", Optional.of(version))
        .database("test_schema")
        .password("test")
        .poolValidationInterval(Duration.ofSeconds(1));
  }

  private static final List<String> versions;

  static {
    if (System.getenv("TRAVIS_BRANCH") != null)
      versions = asList(
          "V9_5",
          "V9_6",
          "V10",
          "V11");
    else
      versions = asList("V11");
  }

  public static final List<Object[]> dataSources = versions.stream()
      .map(v -> new Object[] { DataSource.fromConfig(config(v)), "PG " + v })
      .collect(Collectors.toList());
}
