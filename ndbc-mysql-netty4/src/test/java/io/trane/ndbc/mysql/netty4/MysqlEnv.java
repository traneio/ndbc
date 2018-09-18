package io.trane.ndbc.mysql.netty4;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;

public class MysqlEnv {

  private static final Config config(String version) {
    return Config
        .apply("io.trane.ndbc.mysql.netty4.DataSourceSupplier", "localhost", 0, "test")
        .embedded(Config.Embedded.apply("io.trane.ndbc.mysql.embedded.EmbeddedSupplier", Optional.of(version)))
        .database("test_schema")
        .password("test")
        .poolValidationInterval(Duration.ofSeconds(1))
        .connectionTimeout(Duration.ofSeconds(1))
        .queryTimeout(Duration.ofSeconds(1));
  }

  private static final List<String> versions = Arrays.asList("v5_5_latest", "v5_6_latest", "v5_7_latest");

  public static final List<Object[]> dataSources = versions.stream()
      .map(v -> new Object[] { DataSource.fromConfig(config(v)), "PG " + v })
      .collect(Collectors.toList());
}
