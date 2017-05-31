package io.trane.ndbc.postgres.netty4;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.Connection;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.datasource.DefaultDataSource;
import io.trane.ndbc.datasource.Pool;
import io.trane.ndbc.postgres.encoding.Encoding;
import io.trane.ndbc.postgres.encoding.ValueEncoding;
import io.trane.ndbc.postgres.proto.ExtendedExchange;
import io.trane.ndbc.postgres.proto.ExtendedExecuteExchange;
import io.trane.ndbc.postgres.proto.ExtendedQueryExchange;
import io.trane.ndbc.postgres.proto.QueryResultExchange;
import io.trane.ndbc.postgres.proto.SimpleExecuteExchange;
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

public class DataSourceSupplier implements Supplier<DataSource> {

  private final Config config;
  private final Supplier<Future<Channel>> channelSupplier;
  private final StartupExchange startup = new StartupExchange();
  private final ValueEncoding encoding;

  public DataSourceSupplier(Config config) {
    this.config = config;
    this.encoding = new ValueEncoding(
        config.encodings.stream().map(this::loadEncoding).collect(Collectors.toSet()));
    this.channelSupplier = new ChannelSupplier(config.charset, createSerializer(), new Parser(),
        new NioEventLoopGroup(0, new DefaultThreadFactory("ndbc-netty4", true)), config.host,
        config.port);
  }

  private final Encoding<?> loadEncoding(Class<?> cls) {
    try {
      return (Encoding<?>) cls.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException("Can't load encoding class " + cls + ". Make sure to provide an empty constructor.",
          e);
    }
  }

  private final Serializer createSerializer() {
    return new Serializer(new BindSerializer(encoding), new CancelRequestSerializer(), new CloseSerializer(),
        new DescribeSerializer(), new ExecuteSerializer(), new FlushSerializer(), new ParseSerializer(),
        new QuerySerializer(), new PasswordMessageSerializer(), new StartupMessageSerializer(), new SyncSerializer(),
        new TerminateSerializer());
  }

  private final Supplier<Future<Connection>> createConnection() {
    QueryResultExchange queryResultExchange = new QueryResultExchange(encoding);
    ExtendedExchange extendedExchange = new ExtendedExchange();
    return () -> channelSupplier.get()
        .flatMap(channel -> startup.apply(config.charset, config.user, config.password, config.database).run(channel)
            .map(backendKeyData -> new io.trane.ndbc.postgres.Connection(channel, backendKeyData,
                new SimpleQueryExchange(queryResultExchange),
                new SimpleExecuteExchange(), new ExtendedQueryExchange(queryResultExchange, extendedExchange),
                new ExtendedExecuteExchange(extendedExchange))));
  }

  private Pool<Connection> createPool() {
    return Pool.apply(createConnection(), config.poolMaxSize, config.poolMaxWaiters, config.poolValidationInterval,
        new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("pool-scheduler", true)));
  }

  @Override
  public DataSource get() {
    return new DefaultDataSource(createPool());
  }
}
