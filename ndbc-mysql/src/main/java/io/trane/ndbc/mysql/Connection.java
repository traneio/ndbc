package io.trane.ndbc.mysql;

import io.trane.future.Future;
import io.trane.future.InterruptHandler;
import io.trane.future.Promise;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.mysql.proto.SimpleQueryExchange;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.Exchange;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public final class Connection implements io.trane.ndbc.datasource.Connection {

  private final Channel                                                 channel;
  private final Supplier<? extends Future<? extends Channel>>           channelSupplier;
  private SimpleQueryExchange simpleQueryExchange;

    public Connection(Channel channel, final Supplier<? extends Future<? extends Channel>> channelSupplier, SimpleQueryExchange simpleQueryExchange) {
      this.channel = channel;
      this.channelSupplier = channelSupplier;
      this.simpleQueryExchange = simpleQueryExchange;
    }

    @Override
    public Future<Boolean> isValid() {
        return Future.value(true);
    }

    @Override
    public Future<Void> close() {
        return Future.VOID;
    }

    @Override
    public Future<List<Row>> query(String query) {
      return run(simpleQueryExchange.apply(query));
    }

    @Override
    public Future<Long> execute(String query) {
        return Future.exception(new RuntimeException("Not implemented"));
    }

    @Override
    public Future<List<Row>> query(PreparedStatement query) {
        return Future.exception(new RuntimeException("Not implemented"));
    }

    @Override
    public Future<Long> execute(PreparedStatement query) {
        return Future.exception(new RuntimeException("Not implemented"));
    }

    @Override
    public <R> Future<R> withTransaction(Supplier<Future<R>> sup) {
        return Future.exception(new RuntimeException("Not implemented"));
    }

  private AtomicReference<Future<?>> mutex = new AtomicReference<>();

  private final <T> Future<T> run(final Exchange<T> exchange) {
    Promise<T> next = Promise.create(this::handler);
    Future<?> previous = mutex.getAndSet(next);
    if (previous == null)
      next.become(exchange.run(channel));
    else
      previous.ensure(() -> next.become(exchange.run(channel)));
    return next;
  }

  private final <T> InterruptHandler handler(final Promise<T> p) {
    return ex -> { };
  }
}
