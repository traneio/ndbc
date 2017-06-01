package io.trane.ndbc.postgres;

import java.util.Optional;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.future.InterruptHandler;
import io.trane.future.Promise;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;
import io.trane.ndbc.postgres.proto.ExtendedExecuteExchange;
import io.trane.ndbc.postgres.proto.ExtendedQueryExchange;
import io.trane.ndbc.postgres.proto.Message.BackendKeyData;
import io.trane.ndbc.postgres.proto.Message.CancelRequest;
import io.trane.ndbc.postgres.proto.SimpleExecuteExchange;
import io.trane.ndbc.postgres.proto.SimpleQueryExchange;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.Exchange;

public class Connection implements io.trane.ndbc.Connection {

  private final Channel channel;
  private final Supplier<Future<Channel>> channelSupplier;
  private final Optional<BackendKeyData> backendKeyData;
  private final SimpleQueryExchange simpleQueryExchange;
  private final SimpleExecuteExchange simpleExecuteExchange;
  private final ExtendedQueryExchange extendedQueryExchange;
  private final ExtendedExecuteExchange extendedExecuteExchange;

  public Connection(Channel channel, Supplier<Future<Channel>> channelSupplier, Optional<BackendKeyData> backendKeyData,
      SimpleQueryExchange simpleQueryExchange,
      SimpleExecuteExchange simpleExecuteExchange, ExtendedQueryExchange extendedQueryExchange,
      ExtendedExecuteExchange extendedExecuteExchange) {
    super();
    this.channel = channel;
    this.channelSupplier = channelSupplier;
    this.backendKeyData = backendKeyData;
    this.simpleQueryExchange = simpleQueryExchange;
    this.simpleExecuteExchange = simpleExecuteExchange;
    this.extendedQueryExchange = extendedQueryExchange;
    this.extendedExecuteExchange = extendedExecuteExchange;
  }

  @Override
  public Future<ResultSet> query(String query) {
    return run(simpleQueryExchange.apply(query));
  }

  @Override
  public Future<Integer> execute(String command) {
    return run(simpleExecuteExchange.apply(command));
  }

  @Override
  public Future<ResultSet> query(PreparedStatement query) {
    return run(extendedQueryExchange.apply(query));
  }

  @Override
  public Future<Integer> execute(PreparedStatement command) {
    return run(extendedExecuteExchange.apply(command));
  }


  @Override
  public Future<Boolean> isValid() {
    return query("SELECT 1").map(r -> true).rescue(e -> Future.FALSE);
  }

  @Override
  public Future<Void> close() {
    return Exchange.close().run(channel);
  }

  private <T> Future<T> run(Exchange<T> exchange) {
    return cancellable(exchange.run(channel));
  }

  private <T> Future<T> cancellable(Future<T> fut) {
    return backendKeyData.map(data -> {
      Promise<T> p = Promise.create(v -> handler(v, data));
      fut.proxyTo(p);
      return (Future<T>) p;
    }).orElse(fut);
  }

  private <T> InterruptHandler handler(Promise<T> p, BackendKeyData data) {
    return ex -> channelSupplier.get()
        .flatMap(channel -> Exchange.send(new CancelRequest(data.processId, data.secretKey)).then(Exchange.close())
            .run(channel));
  }
}
