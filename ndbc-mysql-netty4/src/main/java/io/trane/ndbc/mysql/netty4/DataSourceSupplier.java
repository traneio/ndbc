package io.trane.ndbc.mysql.netty4;

import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.mysql.proto.ExtendedExchange;
import io.trane.ndbc.mysql.proto.ExtendedExecuteExchange;
import io.trane.ndbc.mysql.proto.ExtendedQueryExchange;
import io.trane.ndbc.mysql.proto.SimpleExecuteExchange;
import io.trane.ndbc.mysql.proto.SimpleQueryExchange;
import io.trane.ndbc.mysql.proto.StartupExchange;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.netty4.Netty4DataSourceSupplier;
import io.trane.ndbc.netty4.NettyChannel;

public final class DataSourceSupplier extends Netty4DataSourceSupplier {

  private static final Marshallers         marshallers         = new Marshallers();
  private static final Unmarshallers       unmarshallers       = new Unmarshallers();
  private static final SimpleQueryExchange simpleQueryExchange = new SimpleQueryExchange(marshallers, unmarshallers);
  private static final StartupExchange     startup             = new StartupExchange(simpleQueryExchange, marshallers,
      unmarshallers);

  private static final SimpleExecuteExchange   simpleExecuteExchange   = new SimpleExecuteExchange(marshallers,
      unmarshallers);
  private static final ExtendedExchange        extendedExchange        = new ExtendedExchange(marshallers,
      unmarshallers);
  private static final ExtendedQueryExchange   extendedQueryExchange   = new ExtendedQueryExchange(extendedExchange,
      unmarshallers);
  private static final ExtendedExecuteExchange extendedExecuteExchange = new ExtendedExecuteExchange(
      extendedExchange, unmarshallers);

  public DataSourceSupplier(Config config) {
    super(config, createConnection(config), null); // TODO
  }

  private static Function<Supplier<Future<NettyChannel>>, Supplier<Future<Connection>>> createConnection(
      Config config) {
    return (channelSupplier) -> () -> channelSupplier.get().flatMap(channel -> startup
        .apply(config.user(), config.password(), config.database(), "utf8").run(channel).map(connectionId -> {
          return new io.trane.ndbc.mysql.Connection(channel, connectionId, channelSupplier,
              simpleQueryExchange, simpleExecuteExchange, extendedQueryExchange, extendedExecuteExchange);
        }));
  }
}
