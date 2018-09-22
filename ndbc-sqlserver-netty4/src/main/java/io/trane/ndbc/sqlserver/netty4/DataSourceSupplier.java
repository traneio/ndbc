package io.trane.ndbc.sqlserver.netty4;

import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.netty4.Netty4DataSourceSupplier;
import io.trane.ndbc.netty4.NettyChannel;
import io.trane.ndbc.sqlserver.proto.StartupExchange;
import io.trane.ndbc.sqlserver.proto.marshaller.Marshallers;
import io.trane.ndbc.sqlserver.proto.unmarshaller.Unmarshallers;

public class DataSourceSupplier extends Netty4DataSourceSupplier {

  public DataSourceSupplier(final Config config) {
    super(config, createConnection(config), null);
  }

  private static Function<Supplier<Future<NettyChannel>>, Supplier<Future<Connection>>> createConnection(
      final Config config) {
    final Marshallers marshallers = new Marshallers();
    final Unmarshallers unmarshallers = new Unmarshallers();
    final StartupExchange startup = new StartupExchange(marshallers, unmarshallers);

    return (channelSupplier) -> () -> {
      return channelSupplier.get().flatMap(channel -> startup
          .apply(config.user(), config.password(), config.database(), "utf8").run(channel).map(connectionId -> {
            return new io.trane.ndbc.sqlserver.Connection();
          }));
    };
  }
}