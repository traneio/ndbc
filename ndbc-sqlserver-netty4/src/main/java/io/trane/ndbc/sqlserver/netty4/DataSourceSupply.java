package io.trane.ndbc.sqlserver.netty4;

import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.netty4.Netty4DataSourceSupplier;
import io.trane.ndbc.netty4.NettyChannel;

public class DataSourceSupply extends Netty4DataSourceSupplier {

  public DataSourceSupply(final Config config) {
    super(config, createConnection(config), null);
  }

  private static Function<Supplier<Future<NettyChannel>>, Supplier<Future<Connection>>> createConnection(
      final Config config) {
    return null;
  }
}