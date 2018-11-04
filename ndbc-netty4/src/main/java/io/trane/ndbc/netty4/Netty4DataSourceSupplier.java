package io.trane.ndbc.netty4;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.datasource.LockFreePool;
import io.trane.ndbc.datasource.Pool;
import io.trane.ndbc.datasource.PooledDataSource;
import io.trane.ndbc.proto.BufferReader;

public abstract class Netty4DataSourceSupplier
    implements Supplier<DataSource<PreparedStatement, Row>> {

  protected final Config                     config;
  private final Supplier<Future<Connection>> createConnection;

  public Netty4DataSourceSupplier(final Config config,
      final Function<BufferReader, Optional<BufferReader>> transformBufferReader) {
    this.config = config;
    final ChannelSupplier channelSupplier = new ChannelSupplier(
        new NioEventLoopGroup(config.nioThreads().orElse(0), new DefaultThreadFactory("ndbc-netty4", true)),
        config.host(), config.port(), config.charset(), transformBufferReader);
    this.createConnection = createConnectionSupplier(config, channelSupplier);
  }

  protected abstract Supplier<Future<Connection>> createConnectionSupplier(
      Config config, Supplier<Future<NettyChannel>> channelSupplier);

  @Override
  public final DataSource<PreparedStatement, Row> get() {
    final Pool<Connection> pool = LockFreePool.create(createConnection, config.poolMaxSize(),
        config.poolMaxWaiters(),
        config.connectionTimeout(), config.poolValidationInterval(), config.scheduler());
    return new PooledDataSource(pool, config);
  }
}
