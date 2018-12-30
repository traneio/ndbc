package io.trane.ndbc.datasource;

import java.util.List;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.TransactionalDataSource;
import io.trane.ndbc.flow.Flow;

public class ProxyDataSource<P extends PreparedStatement, R extends Row> implements DataSource<P, R> {

  private final DataSource<P, R> underlying;

  public ProxyDataSource(final DataSource<P, R> underlying) {
    this.underlying = underlying;
  }

  @Override
  public Future<List<R>> query(final String query) {
    return underlying.query(query);
  }

  @Override
  public Future<Long> execute(final String statement) {
    return underlying.execute(statement);
  }

  @Override
  public Future<List<R>> query(final P query) {
    return underlying.query(query);
  }

  @Override
  public Flow<R> stream(P query) {
    return underlying.stream(query);
  }

  @Override
  public Future<Long> execute(final P statement) {
    return underlying.execute(statement);
  }

  @Override
  public <T> Future<T> transactional(final Supplier<Future<T>> supplier) {
    return underlying.transactional(supplier);
  }

  @Override
  public TransactionalDataSource<P, R> transactional() {
    return underlying.transactional();
  }

  @Override
  public Future<Void> close() {
    return underlying.close();
  }

  @Override
  public Config config() {
    return underlying.config();
  }
}
