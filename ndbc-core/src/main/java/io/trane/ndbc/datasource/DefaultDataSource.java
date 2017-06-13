package io.trane.ndbc.datasource;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.future.Local;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public final class DefaultDataSource implements DataSource {

  private final Pool<Connection> pool;
  private final Local<Connection> currentTransation;

  public DefaultDataSource(final Pool<Connection> pool) {
    super();
    this.pool = pool;
    this.currentTransation = Local.apply();
  }

  @Override
  public final Future<Iterable<Row>> query(final String query) {
    return withConnection(c -> c.query(query));
  }

  @Override
  public final Future<Integer> execute(final String statement) {
    return withConnection(c -> c.execute(statement));
  }

  @Override
  public final Future<Iterable<Row>> query(final PreparedStatement query) {
    return withConnection(c -> c.query(query));
  }

  @Override
  public final Future<Integer> execute(final PreparedStatement statement) {
    return withConnection(c -> c.execute(statement));
  }

  @Override
  public final <R> Future<R> transactional(final Supplier<Future<R>> supplier) {
    if (currentTransation.get().isPresent())
      return Future.flatApply(supplier);
    else
      return pool.apply(c -> {
        currentTransation.set(Optional.of(c));
        return c.withTransaction(supplier).ensure(() -> currentTransation.set(Optional.empty()));
      });
  }

  private final <R> Future<R> withConnection(final Function<Connection, Future<R>> f) {
    Optional<Connection> transaction = currentTransation.get();
    if (transaction.isPresent())
      return f.apply(transaction.get());
    else
      return pool.apply(f);
  }

  @Override
  public final Future<Void> close() {
    return pool.close();
  }
}
