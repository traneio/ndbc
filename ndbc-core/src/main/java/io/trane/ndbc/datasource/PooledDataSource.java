package io.trane.ndbc.datasource;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.future.Local;
import io.trane.future.Transformer;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.TransactionalDataSource;

public final class PooledDataSource implements DataSource<PreparedStatement, Row> {

  private final Pool<Connection>  pool;
  private final Local<Connection> currentTransaction;
  private final Config            config;

  public PooledDataSource(final Pool<Connection> pool, Config config) {
    this.pool = pool;
    this.config = config;
    currentTransaction = Local.apply();
  }

  @Override
  public final Future<List<Row>> query(final String query) {
    return withConnection(c -> c.query(query));
  }

  @Override
  public final Future<Long> execute(final String statement) {
    return withConnection(c -> c.execute(statement));
  }

  @Override
  public final Future<List<Row>> query(final PreparedStatement query) {
    return withConnection(c -> c.query(query));
  }

  @Override
  public final Future<Long> execute(final PreparedStatement statement) {
    return withConnection(c -> c.execute(statement));
  }

  @Override
  public final <T> Future<T> transactional(final Supplier<Future<T>> supplier) {
    if (currentTransaction.get().isPresent())
      return Future.flatApply(supplier);
    else
      return pool.acquire().flatMap(c -> {
        currentTransaction.set(Optional.of(c));
        return c.beginTransaction()
            .flatMap(v -> supplier.get())
            .transformWith(new Transformer<T, Future<T>>() {
              @Override
              public Future<T> onException(final Throwable ex) {
                currentTransaction.set(Optional.empty());
                return c.rollback().flatMap(v -> Future.exception(ex));
              }

              @Override
              public Future<T> onValue(final T value) {
                currentTransaction.set(Optional.empty());
                return c.commit().map(v -> value);
              }
            }).ensure(() -> pool.release(c));
      });
  }

  @Override
  public TransactionalDataSource<PreparedStatement, Row> transactional() {
    final Future<Connection> conn = currentTransaction.get()
        .map(Future::value).orElseGet(() -> pool.acquire());

    return new TransactionalDataSource<PreparedStatement, Row>() {

      @Override
      public Future<List<Row>> query(String query) {
        return conn.flatMap(c -> c.query(query));
      }

      @Override
      public Future<Long> execute(String statement) {
        return conn.flatMap(c -> c.execute(statement));
      }

      @Override
      public Future<List<Row>> query(PreparedStatement query) {
        return conn.flatMap(c -> c.query(query));
      }

      @Override
      public Future<Long> execute(PreparedStatement statement) {
        return conn.flatMap(c -> c.execute(statement));
      }

      @Override
      public <T> Future<T> transactional(Supplier<Future<T>> supplier) {
        return conn.flatMap(c -> {
          if (currentTransaction.get().isPresent())
            return Future.flatApply(supplier);
          else {
            currentTransaction.set(Optional.of(c));
            return Future.flatApply(supplier)
                .ensure(() -> currentTransaction.set(Optional.empty()));
          }

        });
      }

      @Override
      public TransactionalDataSource<PreparedStatement, Row> transactional() {
        return this;
      }

      @Override
      public Future<Void> close() {
        if (!currentTransaction.get().isPresent())
          conn.onSuccess(pool::release);
        return Future.VOID;
      }

      @Override
      public Config config() {
        return config;
      }

      @Override
      public Future<Void> commit() {
        return conn.flatMap(c -> c.commit());
      }

      @Override
      public Future<Void> rollback() {
        return conn.flatMap(c -> c.rollback());
      }
    };
  }

  @Override
  public final Future<Void> close() {
    return pool.close();
  }

  private final <T> Future<T> withConnection(final Function<Connection, Future<T>> f) {
    final Optional<Connection> transaction = currentTransaction.get();
    if (transaction.isPresent())
      return f.apply(transaction.get());
    else
      return pool.acquire().flatMap(c -> {
        return Future.flatApply(() -> f.apply(c))
            .ensure(() -> pool.release(c));
      });
  }

  @Override
  public Config config() {
    return this.config;
  }
}
