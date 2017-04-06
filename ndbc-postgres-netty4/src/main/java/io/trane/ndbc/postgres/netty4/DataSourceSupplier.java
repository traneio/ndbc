package io.trane.ndbc.postgres.netty4;

import java.util.function.Supplier;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.datasource.DefaultDataSource;
import io.trane.ndbc.datasource.Pool;
import io.trane.ndbc.postgres.decoder.Decoder;
import io.trane.ndbc.postgres.encoder.Encoder;
import io.trane.ndbc.postgres.proto.Startup;

public class DataSourceSupplier implements Supplier<DataSource<Connection>> {

  private final Config config;
  private final Supplier<Future<Channel>> channelSupplier;

  public DataSourceSupplier(Config config) {
    this.config = config;
    this.channelSupplier = new ChannelSupplier(config.charset, new Encoder(), new Decoder(),
        new NioEventLoopGroup(0, new DefaultThreadFactory("ndbc-netty4", true)), config.host,
        config.port);
  }

  private final Supplier<Future<Connection>> createConnection() {
    return () -> channelSupplier.get()
        .flatMap(channel -> Startup.apply(config.charset, config.user, config.password, config.database).run(channel)
            .map(backendKeyData -> new Connection(channel, backendKeyData)));
  }

  private Pool<Connection> createPool() {
    return Pool.apply(createConnection(), config.poolMaxSize, config.poolMaxWaiters, config.poolValidationInterval);
  }

  @Override
  public DataSource<Connection> get() {
    return new DefaultDataSource<>(createPool());
  }
}
