package io.trane.ndbc.mysql.netty4;

import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.mysql.proto.ExtendedExecuteExchange;
import io.trane.ndbc.mysql.proto.ExtendedQueryExchange;
import io.trane.ndbc.mysql.proto.SimpleExecuteExchange;
import io.trane.ndbc.mysql.proto.SimpleQueryExchange;
import io.trane.ndbc.mysql.proto.StartupExchange;
import io.trane.ndbc.mysql.proto.marshaller.MysqlMarshaller;
import io.trane.ndbc.mysql.proto.unmarshaller.MysqlUnmarshaller;
import io.trane.ndbc.netty4.Netty4DataSourceSupplier;
import io.trane.ndbc.netty4.NettyChannel;

public final class DataSourceSupplier extends Netty4DataSourceSupplier {

  private static final StartupExchange startup = new StartupExchange(new SimpleQueryExchange());

  public DataSourceSupplier(Config config) {
    super(config, new MysqlMarshaller(), new MysqlUnmarshaller(), createConnection(config));
  }

  private static Function<Supplier<Future<NettyChannel>>, Supplier<Future<Connection>>> createConnection(
      Config config) {
    return (channelSupplier) -> () -> channelSupplier.get()
        .flatMap(channel -> startup.apply(config.user(), config.password(), config.database(), "utf8")
            .run(channel)
            .map(connectionId -> new io.trane.ndbc.mysql.Connection(channel, connectionId, channelSupplier,
                new SimpleQueryExchange(), new SimpleExecuteExchange(), new ExtendedQueryExchange(),
                new ExtendedExecuteExchange())));

  }
}
