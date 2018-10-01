package io.trane.ndbc.sqlserver.netty4;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;

public class SqlServerEnv {

  private static final Config config(final String version) {
    return Config.apply("io.trane.ndbc.sqlserver.netty4.DataSourceSupplier", "localhost", 1433, "sa")
        .password("Juli@no12345").poolValidationInterval(Duration.ofSeconds(1))
        .connectionTimeout(Duration.ofSeconds(1));
  }

  private static final List<String> versions = Arrays.asList("Developer");

  public static final List<Object[]> dataSources = versions.stream()
      .map(v -> new Object[] { DataSource.fromConfig(config(v)), v }).collect(Collectors.toList());
}
