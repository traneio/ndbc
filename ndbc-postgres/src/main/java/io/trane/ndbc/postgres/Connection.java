package io.trane.ndbc.postgres;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.future.InterruptHandler;
import io.trane.future.Promise;
import io.trane.future.Transformer;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.proto.ExtendedExecuteExchange;
import io.trane.ndbc.postgres.proto.ExtendedQueryExchange;
import io.trane.ndbc.postgres.proto.Message.BackendKeyData;
import io.trane.ndbc.postgres.proto.Message.CancelRequest;
import io.trane.ndbc.postgres.proto.SimpleExecuteExchange;
import io.trane.ndbc.postgres.proto.SimpleQueryExchange;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.Exchange;

public final class Connection implements io.trane.ndbc.datasource.Connection {

  private final Channel                                       channel;
  private final Supplier<? extends Future<? extends Channel>> channelSupplier;
  private final Optional<BackendKeyData>                      backendKeyData;
  private final SimpleQueryExchange                           simpleQueryExchange;
  private final SimpleExecuteExchange                         simpleExecuteExchange;
  private final ExtendedQueryExchange                         extendedQueryExchange;
  private final ExtendedExecuteExchange                       extendedExecuteExchange;

  public Connection(final Channel channel, final Supplier<? extends Future<? extends Channel>> channelSupplier,
      final Optional<BackendKeyData> backendKeyData, final SimpleQueryExchange simpleQueryExchange,
      final SimpleExecuteExchange simpleExecuteExchange, final ExtendedQueryExchange extendedQueryExchange,
      final ExtendedExecuteExchange extendedExecuteExchange) {
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
  public final Future<List<Row>> query(final String query) {
    return run(simpleQueryExchange.apply(query));
  }

  @Override
  public final Future<Integer> execute(final String command) {
    return run(simpleExecuteExchange.apply(command));
  }

  @Override
  public final Future<List<Row>> query(final PreparedStatement query) {
    return run(extendedQueryExchange.apply(query.query(), query.params()));
  }

  @Override
  public final Future<Integer> execute(final PreparedStatement command) {
    return run(extendedExecuteExchange.apply(command.query(), command.params()));
  }

  @Override
  public final Future<Boolean> isValid() {
    return query("SELECT 1").map(r -> true).rescue(e -> Future.FALSE);
  }

  @Override
  public final Future<Void> close() {
    return Exchange.CLOSE.run(channel);
  }

  @Override
  public <R> Future<R> withTransaction(Supplier<Future<R>> sup) {
    return execute("BEGIN").flatMap(v -> sup.get()).transformWith(new Transformer<R, Future<R>>() {
      @Override
      public Future<R> onException(final Throwable ex) {
        return execute("ROLLBACK").flatMap(v -> Future.exception(ex));
      }

      @Override
      public Future<R> onValue(final R value) {
        return execute("COMMIT").map(v -> value);
      }
    });
  }

  private final <T> Future<T> run(final Exchange<T> exchange) {
    return cancellable(exchange.run(channel));
  }

  private final <T> Future<T> cancellable(final Future<T> fut) {
    return backendKeyData.map(data -> {
      final Promise<T> p = Promise.create(v -> handler(v, data));
      fut.proxyTo(p);
      return (Future<T>) p;
    }).orElse(fut);
  }

  private final <T> InterruptHandler handler(final Promise<T> p, final BackendKeyData data) {
    return ex -> channelSupplier.get().flatMap(channel -> Exchange
        .send(new CancelRequest(data.processId, data.secretKey)).then(Exchange.CLOSE).run(channel));
  }
}
