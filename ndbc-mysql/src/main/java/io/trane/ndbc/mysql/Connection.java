package io.trane.ndbc.mysql;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.trane.future.Future;
import io.trane.future.InterruptHandler;
import io.trane.future.Promise;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.NonFatalException;
import io.trane.ndbc.value.Value;

public final class Connection implements io.trane.ndbc.datasource.Connection {

  private static final Logger log = LoggerFactory.getLogger(Connection.class);

  private static final PreparedStatement isValidQuery = PreparedStatement.create("SELECT 1");

  private final Channel                                                 channel;
  private final Long                                                    connectionId;
  private final Optional<Duration>                                      queryTimeout;
  private final ScheduledExecutorService                                scheduler;
  private final Function<String, Exchange<List<Row>>>                   simpleQueryExchange;
  private final Function<String, Exchange<Long>>                        simpleExecuteExchange;
  private final BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange;
  private final BiFunction<String, List<Value<?>>, Exchange<Long>>      extendedExecuteExchange;
  private final Supplier<DataSource<PreparedStatement, Row>>            dataSourceSupplier;

  public Connection(final Channel channel, final Long connectionId, final Marshallers marshallers,
      final Optional<Duration> queryTimeout, final ScheduledExecutorService scheduler,
      final Function<String, Exchange<List<Row>>> simpleQueryExchange,
      final Function<String, Exchange<Long>> simpleExecuteExchange,
      final BiFunction<String, List<Value<?>>, Exchange<List<Row>>> extendedQueryExchange,
      final BiFunction<String, List<Value<?>>, Exchange<Long>> extendedExecuteExchange,
      final Supplier<DataSource<PreparedStatement, Row>> dataSourceSupplier) {
    this.channel = channel;
    this.connectionId = connectionId;
    this.queryTimeout = queryTimeout;
    this.scheduler = scheduler;
    this.simpleQueryExchange = simpleQueryExchange;
    this.simpleExecuteExchange = simpleExecuteExchange;
    this.extendedQueryExchange = extendedQueryExchange;
    this.extendedExecuteExchange = extendedExecuteExchange;
    this.dataSourceSupplier = dataSourceSupplier;
  }

  @Override
  public Future<Boolean> isValid() {
    return query(isValidQuery).map(r -> true).rescue(e -> Future.FALSE);
  }

  @Override
  public Future<Void> close() {
    return Exchange.CLOSE.run(channel);
  }

  @Override
  public Future<List<Row>> query(final String query) {
    return run(simpleQueryExchange.apply(query));
  }

  @Override
  public Future<Long> execute(final String command) {
    return run(simpleExecuteExchange.apply(command));
  }

  @Override
  public final Future<List<Row>> query(final PreparedStatement query) {
    return run(extendedQueryExchange.apply(query.query(), query.params()));
  }

  @Override
  public Publisher<Row> stream(PreparedStatement query) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Future<Long> execute(final PreparedStatement command) {
    return run(extendedExecuteExchange.apply(command.query(), command.params()));
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
    return ex -> {
      final DataSource<PreparedStatement, Row> ds = dataSourceSupplier.get();
      ds.execute("KILL QUERY " + connectionId)
          .onFailure(e -> log.warn("Can't cancel request. Reason: " + e))
          .ensure(() -> ds.close());
    };
  }

  @Override
  public Future<Void> beginTransaction() {
    return execute("BEGIN").voided();
  }

  @Override
  public Future<Void> commit() {
    return execute("COMMIT").voided();
  }

  @Override
  public Future<Void> rollback() {
    return execute("ROLLBACK").voided();
  }
}
