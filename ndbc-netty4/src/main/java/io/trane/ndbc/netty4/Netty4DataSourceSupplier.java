package io.trane.ndbc.netty4;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Function;
import java.util.function.Supplier;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.datasource.LockFreePool;
import io.trane.ndbc.datasource.Pool;
import io.trane.ndbc.datasource.PooledDataSource;
import io.trane.ndbc.proto.Marshaller;
import io.trane.ndbc.proto.Unmarshaller;

public abstract class Netty4DataSourceSupplier implements Supplier<DataSource> {

  protected final Config                     config;
  private final Supplier<Future<Connection>> createConnection;

  public Netty4DataSourceSupplier(final Config config, Marshaller marshaller, Unmarshaller unmarshaller,
      Function<Supplier<Future<NettyChannel>>, Supplier<Future<Connection>>> createConnectionSupplier) {
    this.config = config;
    ChannelSupplier channelSupplier = new ChannelSupplier(config.charset(), marshaller, unmarshaller,
        new NioEventLoopGroup(config.nioThreads().orElse(0), new DefaultThreadFactory("ndbc-netty4", true)),
        config.host(), config.port());
    this.createConnection = createConnectionSupplier.apply(channelSupplier);
  }

  @Override
  public final DataSource get() {
    Pool<Connection> pool = LockFreePool.apply(createConnection, config.poolMaxSize(), config.poolMaxWaiters(),
        config.poolValidationInterval(),
        new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("ndbc-pool-scheduler", true)));
    return new PooledDataSource(pool);
  }
}
