package io.trane.ndbc.mysql;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.future.InterruptHandler;
import io.trane.future.Promise;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class Connection implements io.trane.ndbc.datasource.Connection {

  private final Channel channel;
  private final Supplier<? extends Future<? extends Channel>> channelSupplier;
  private final Function<String, Exchange<List<Row>>> simpleQueryExchange;
  private final Function<String, Exchange<Long>> simpleExecuteExchange;
  private final BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange;
  private final BiFunction<String, List<Value<?>>, Exchange<Long>> extendedExecuteExchange;

  public Connection(final Channel channel, final Supplier<? extends Future<? extends Channel>> channelSupplier,
      final Function<String, Exchange<List<Row>>> simpleQueryExchange,
      final Function<String, Exchange<Long>> simpleExecuteExchange,
      final BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange,
      final BiFunction<String, List<Value<?>>, Exchange<Long>> extendedExecuteExchange) {
    this.channel = channel;
    this.channelSupplier = channelSupplier;
    this.simpleQueryExchange = simpleQueryExchange;
    this.simpleExecuteExchange = simpleExecuteExchange;
    this.extendedQueryExchange = extendedQueryExchange;
    this.extendedExecuteExchange = extendedExecuteExchange;
  }

  @Override
  public Future<Boolean> isValid() {
    return Future.value(true); // TODO
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
  public Future<Long> execute(String command) {
    return run(simpleExecuteExchange.apply(command));
  }

  @Override
  public final Future<List<Row>> query(final PreparedStatement query) {
    return run(extendedQueryExchange.apply(query.query(), query.params()));
  }

  @Override
  public Future<Long> execute(PreparedStatement command) {
    return run(extendedExecuteExchange.apply(command.query(), command.params()));
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
    return ex -> {};
  }
}
