package io.trane.ndbc;

import java.util.function.Supplier;

import io.trane.future.Future;

public interface Connection {
  
  Future<Boolean> isValid();
  
  Future<Void> close();

  Future<ResultSet> query(String query);

  Future<Integer> execute(String query);

  Future<ResultSet> query(PreparedStatement query);

  Future<Integer> execute(PreparedStatement query);

  <R> Future<R> withTransaction(Supplier<Future<R>> sup);
}
