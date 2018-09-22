package io.trane.ndbc.sqlserver;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Logger;

import io.trane.future.Future;
import io.trane.future.InterruptHandler;
import io.trane.future.Promise;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.sqlserver.proto.Message.Attention;
import io.trane.ndbc.sqlserver.proto.marshaller.Marshallers;
import io.trane.ndbc.util.NonFatalException;

public final class Connection implements io.trane.ndbc.datasource.Connection {

  private static final Logger logger = Logger.getLogger(Connection.class.getName());

  private final Channel channel;
  private final Marshallers marshallers;
  private final Optional<Duration> queryTimeout;
  private final ScheduledExecutorService scheduler;
  private final Supplier<? extends Future<? extends Channel>> channelSupplier;
  private final Function<String, Exchange<List<Row>>> simpleQueryExchange;

  public Connection(final Channel channel, final Marshallers marshallers, final Optional<Duration> queryTimeout,
      final ScheduledExecutorService scheduler, final Supplier<? extends Future<? extends Channel>> channelSupplier,
      final Function<String, Exchange<List<Row>>> simpleQueryExchange) {
    this.channel = channel;
    this.marshallers = marshallers;
    this.queryTimeout = queryTimeout;
    this.scheduler = scheduler;
    this.channelSupplier = channelSupplier;
    this.simpleQueryExchange = simpleQueryExchange;
  }

  @Override
  public Future<Boolean> isValid() {
    return null;
  }

  @Override
  public Future<Void> close() {
    return null;
  }

  @Override
  public Future<List<Row>> query(final String query) {
    return run(simpleQueryExchange.apply(query));
  }

  @Override
  public Future<Long> execute(final String query) {
    return null;
  }

  @Override
  public Future<List<Row>> query(final PreparedStatement query) {
    return null;
  }

  @Override
  public Future<Long> execute(final PreparedStatement query) {
    return null;
  }

  @Override
  public <R> Future<R> withTransaction(final Supplier<Future<R>> sup) {
    return null;
  }

  private final AtomicReference<Future<?>> mutex = new AtomicReference<>();

  private final <T> Future<T> run(final Exchange<T> exchange) {
    final Promise<T> next = Promise.create(this::handler);
    final Future<?> previous = mutex.getAndSet(next);
    if (previous == null)
      next.become(execute(exchange));
    else
      previous.ensure(() -> next.become(execute(exchange)));
    return next;
  }

  private final <T> Future<T> execute(final Exchange<T> exchange) {
    try {
      final Future<T> run = exchange.run(channel);
      return queryTimeout.map(t -> run.within(t, scheduler)).orElse(run);
    } catch (final Throwable t) {
      NonFatalException.verify(t);
      return Future.exception(t);
    }
  }

  private final <T> InterruptHandler handler(final Promise<T> p) {
    return ex -> channelSupplier.get()
        .flatMap(channel -> Exchange.send(marshallers.attention, new Attention()).then(Exchange.CLOSE).run(channel))
        .onFailure(e -> logger.warning("Can't cancel request. Reason: " + e));
  }
}
