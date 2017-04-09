package io.trane.ndbc.postgres.netty4;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Supplier;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.datasource.DefaultDataSource;
import io.trane.ndbc.datasource.Pool;
import io.trane.ndbc.postgres.Connection;
import io.trane.ndbc.postgres.encoding.ValueEncoding;
import io.trane.ndbc.postgres.proto.ExecuteExchange;
import io.trane.ndbc.postgres.proto.SimpleQueryExchange;
import io.trane.ndbc.postgres.proto.StartupExchange;
import io.trane.ndbc.postgres.proto.parser.Parser;
import io.trane.ndbc.postgres.proto.serializer.BindSerializer;
import io.trane.ndbc.postgres.proto.serializer.CancelRequestSerializer;
import io.trane.ndbc.postgres.proto.serializer.CloseSerializer;
import io.trane.ndbc.postgres.proto.serializer.DescribeSerializer;
import io.trane.ndbc.postgres.proto.serializer.ExecuteSerializer;
import io.trane.ndbc.postgres.proto.serializer.FlushSerializer;
import io.trane.ndbc.postgres.proto.serializer.ParseSerializer;
import io.trane.ndbc.postgres.proto.serializer.PasswordMessageSerializer;
import io.trane.ndbc.postgres.proto.serializer.QuerySerializer;
import io.trane.ndbc.postgres.proto.serializer.Serializer;
import io.trane.ndbc.postgres.proto.serializer.StartupMessageSerializer;
import io.trane.ndbc.postgres.proto.serializer.SyncSerializer;
import io.trane.ndbc.postgres.proto.serializer.TerminateSerializer;

public class DataSourceSupplier implements Supplier<DataSource<Connection>> {

  private final Config config;
  private final Supplier<Future<Channel>> channelSupplier;
  private final StartupExchange startup = new StartupExchange();
  private final ValueEncoding encoding = new ValueEncoding();

  public DataSourceSupplier(Config config) {
    this.config = config;
    this.channelSupplier = new ChannelSupplier(config.charset, createSerializer(), new Parser(),
        new NioEventLoopGroup(0, new DefaultThreadFactory("ndbc-netty4", true)), config.host,
        config.port);
  }

  private final Serializer createSerializer() {
    return new Serializer(new BindSerializer(encoding), new CancelRequestSerializer(), new CloseSerializer(),
        new DescribeSerializer(), new ExecuteSerializer(), new FlushSerializer(), new ParseSerializer(),
        new QuerySerializer(), new PasswordMessageSerializer(), new StartupMessageSerializer(), new SyncSerializer(),
        new TerminateSerializer());
  }

  private final Supplier<Future<Connection>> createConnection() {
    return () -> channelSupplier.get()
        .flatMap(channel -> startup.apply(config.charset, config.user, config.password, config.database).run(channel)
            .map(backendKeyData -> new Connection(channel, backendKeyData, new SimpleQueryExchange(encoding),
                new ExecuteExchange())));
  }

  private Pool<Connection> createPool() {
    return Pool.apply(createConnection(), config.poolMaxSize, config.poolMaxWaiters, config.poolValidationInterval,
        new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("pool-scheduler", true)));
  }

  @Override
  public DataSource<Connection> get() {
    return new DefaultDataSource<>(createPool());
  }
}
