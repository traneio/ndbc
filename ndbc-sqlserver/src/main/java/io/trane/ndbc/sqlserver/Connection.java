package io.trane.ndbc.sqlserver;

import java.util.List;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public final class Connection implements io.trane.ndbc.datasource.Connection {

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
    return null;
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
}
