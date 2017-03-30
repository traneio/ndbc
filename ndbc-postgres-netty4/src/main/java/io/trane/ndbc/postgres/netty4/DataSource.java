package io.trane.ndbc.postgres.netty4;

import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.datasource.BaseDataSource;
import io.trane.ndbc.datasource.Pool;

public class DataSource extends BaseDataSource<Connection> {

  private final Config config;

  public DataSource(Config config) {
    super(createPool());
    this.config = config;
  }

  private final Supplier<Future<Connection>> createConnection() {
    return () -> {
      return null;
    };
  }

  private static Pool<Connection> createPool() {
    return null;
  }

}
