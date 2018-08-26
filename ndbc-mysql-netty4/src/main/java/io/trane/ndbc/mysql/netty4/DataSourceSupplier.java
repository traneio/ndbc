package io.trane.ndbc.mysql.netty4;

import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.datasource.Connection;
import io.trane.ndbc.mysql.proto.BinaryResultSetExchange;
import io.trane.ndbc.mysql.proto.ExtendedExchange;
import io.trane.ndbc.mysql.proto.ExtendedExecuteExchange;
import io.trane.ndbc.mysql.proto.ExtendedQueryExchange;
import io.trane.ndbc.mysql.proto.Message.BinaryResultSet;
import io.trane.ndbc.mysql.proto.SimpleExecuteExchange;
import io.trane.ndbc.mysql.proto.SimpleQueryExchange;
import io.trane.ndbc.mysql.proto.StartupExchange;
import io.trane.ndbc.mysql.proto.TerminatorExchange;
import io.trane.ndbc.mysql.proto.TextResultSetExchange;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.mysql.proto.unmarshaller.TransformBufferReader;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.netty4.Netty4DataSourceSupplier;
import io.trane.ndbc.netty4.NettyChannel;

public final class DataSourceSupplier extends Netty4DataSourceSupplier {

  public DataSourceSupplier(Config config) {
    super(config, createConnection(config), new TransformBufferReader()); // TODO
  }

  private static Function<Supplier<Future<NettyChannel>>, Supplier<Future<Connection>>> createConnection(
      Config config) {

    final Unmarshallers unmarshallers = new Unmarshallers(config.charset());
    final TerminatorExchange terminatorExchange = new TerminatorExchange(unmarshallers);
    final TextResultSetExchange textResultSetExchange = new TextResultSetExchange(unmarshallers);
    final BinaryResultSetExchange binaryResultSetExchange = new BinaryResultSetExchange(unmarshallers);
    final Marshallers marshallers = new Marshallers();
    final SimpleQueryExchange simpleQueryExchange = new SimpleQueryExchange(marshallers,
        textResultSetExchange.apply());
    final StartupExchange startup = new StartupExchange(simpleQueryExchange,
        marshallers, unmarshallers, terminatorExchange.okPacketVoid);

    final SimpleExecuteExchange simpleExecuteExchange = new SimpleExecuteExchange(marshallers,
        terminatorExchange.affectedRows);
    final ExtendedExchange extendedExchange = new ExtendedExchange(marshallers,
        terminatorExchange.affectedRows);
    final ExtendedQueryExchange extendedQueryExchange = new ExtendedQueryExchange(
        extendedExchange, binaryResultSetExchange.apply());
    final ExtendedExecuteExchange extendedExecuteExchange = new ExtendedExecuteExchange(
        extendedExchange, terminatorExchange.affectedRows);

    return (channelSupplier) -> () -> channelSupplier.get().flatMap(channel -> startup
        .apply(config.user(), config.password(), config.database(), "utf8").run(channel).map(connectionId -> {
          return new io.trane.ndbc.mysql.Connection(channel, connectionId, channelSupplier,
              simpleQueryExchange, simpleExecuteExchange, extendedQueryExchange, extendedExecuteExchange);
        }));
  }
}
