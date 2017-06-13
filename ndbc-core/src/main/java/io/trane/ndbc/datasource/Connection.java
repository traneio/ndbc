package io.trane.ndbc.datasource;

import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public interface Connection {

  Future<Boolean> isValid();

  Future<Void> close();

  Future<Iterable<Row>> query(String query);

  Future<Integer> execute(String query);

  Future<Iterable<Row>> query(PreparedStatement query);

  Future<Integer> execute(PreparedStatement query);

  <R> Future<R> withTransaction(final Supplier<Future<R>> sup);
}
