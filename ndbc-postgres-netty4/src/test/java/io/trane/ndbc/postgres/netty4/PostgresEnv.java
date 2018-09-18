package io.trane.ndbc.postgres.netty4;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.postgres.embedded.Embedded;
import io.trane.ndbc.postgres.embedded.Embedded.Version;

public class PostgresEnv {

  private static final Config config() {
    return Config
        .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier",
            "localhost", Embedded.findFreePort(), "postgres")
        .password("postgres")
        .poolValidationInterval(Duration.ofSeconds(1))
        .connectionTimeout(Duration.ofSeconds(1))
        .queryTimeout(Duration.ofSeconds(1));
  }

  public static final List<Object[]> dataSources;

  static {
    List<Version> versions = Arrays.asList(Embedded.Version.values());
    List<Future<DataSource>> list = versions.stream()
        .map(v -> Embedded.dataSource(v, config()))
        .collect(Collectors.toList());
    try {
      dataSources = new ArrayList<>();
      List<DataSource> values = Future.collect(list).get(Duration.ofHours(1));
      for (int i = 0; i < values.size(); i++) {
        dataSources.add(new Object[] { values.get(i), "PG " + versions.get(i) });
      }
    } catch (CheckedFutureException e) {
      throw new RuntimeException(e);
    }
  }
}