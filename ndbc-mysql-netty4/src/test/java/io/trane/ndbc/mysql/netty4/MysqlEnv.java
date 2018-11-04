package io.trane.ndbc.mysql.netty4;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;

public class MysqlEnv {

  private static final Config config(final String version) {
    return Config
        .create("io.trane.ndbc.mysql.netty4.DataSourceSupplier", "localhost", 0, "test")
        .embedded(Config.Embedded.create("io.trane.ndbc.mysql.embedded.EmbeddedSupplier", Optional.of(version)))
        .database("test_schema")
        .password("test")
        .poolValidationInterval(Duration.ofSeconds(1));
  }

  private static final List<String> versions;

  static {
    if (System.getenv("TRAVIS_BRANCH") != null)
      versions = Arrays.asList(
          "v5_5_latest",
          "v5_6_latest",
          "v5_7_latest");
    else
      versions = Arrays.asList("v5_7_latest");
  }

  public static final List<Object[]> dataSources = versions.stream()
      .map(v -> new Object[] { DataSource.fromConfig(config(v)), "Mysql " + v })
      .collect(Collectors.toList());
}
