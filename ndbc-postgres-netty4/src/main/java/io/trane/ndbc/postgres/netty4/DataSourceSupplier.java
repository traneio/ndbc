package io.trane.ndbc.postgres.netty4;

import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.netty4.InitSSLHandler;
import io.trane.ndbc.netty4.Netty4DataSourceSupplier;
import io.trane.ndbc.netty4.NettyChannel;
import io.trane.ndbc.postgres.encoding.EncodingRegistry;
import io.trane.ndbc.postgres.proto.ExtendedExchange;
import io.trane.ndbc.postgres.proto.ExtendedExecuteExchange;
import io.trane.ndbc.postgres.proto.ExtendedQueryExchange;
import io.trane.ndbc.postgres.proto.InitSSLExchange;
import io.trane.ndbc.postgres.proto.QueryResultExchange;
import io.trane.ndbc.postgres.proto.SimpleExecuteExchange;
import io.trane.ndbc.postgres.proto.SimpleQueryExchange;
import io.trane.ndbc.postgres.proto.StartupExchange;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.TransformBufferReader;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;

public final class DataSourceSupplier extends Netty4DataSourceSupplier {

  private final static InitSSLHandler initSSLHandler = new InitSSLHandler();

  public DataSourceSupplier(final Config config) {
    super(config, createConnection(config), new TransformBufferReader());
  }

  private static Function<Supplier<Future<NettyChannel>>, Supplier<Future<Connection>>> createConnection(
      final Config config) {
    final EncodingRegistry encoding = new EncodingRegistry(config.loadCustomEncodings(), config.charset());
    final Marshallers marshallers = new Marshallers(encoding);
    final Unmarshallers unmarshallers = new Unmarshallers(config.charset());
    final QueryResultExchange queryResultExchange = new QueryResultExchange(encoding, unmarshallers);
    final InitSSLExchange initSSLExchange = new InitSSLExchange(marshallers, unmarshallers);
    final StartupExchange startup = new StartupExchange(marshallers, unmarshallers);
    return (channelSupplier) -> () -> {
      final ExtendedExchange extendedExchange = new ExtendedExchange(marshallers, unmarshallers);
      return channelSupplier.get().flatMap(channel -> initSSLExchange.apply(config.ssl()).run(channel)
          .flatMap(ssl -> initSSLHandler.apply(config.host(), config.port(), ssl, channel))
          .flatMap(v -> startup.apply(config.charset(), config.user(), config.password(), config.database())
              .run(channel)
              .map(backendKeyData -> new io.trane.ndbc.postgres.Connection(channel, marshallers,
                  config.queryTimeout(), config.scheduler(),
                  channelSupplier, backendKeyData,
                  new SimpleQueryExchange(queryResultExchange, marshallers, unmarshallers),
                  new SimpleExecuteExchange(marshallers, unmarshallers),
                  new ExtendedQueryExchange(queryResultExchange, extendedExchange),
                  new ExtendedExecuteExchange(extendedExchange, unmarshallers)))));
    };
  }
}
