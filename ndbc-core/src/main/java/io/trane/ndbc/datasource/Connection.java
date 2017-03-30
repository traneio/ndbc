package io.trane.ndbc.datasource;

import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;

public interface Connection {

  Future<ResultSet> query(String query);

  Future<Integer> execute(String query);

  Future<ResultSet> query(PreparedStatement query);

  Future<Integer> execute(PreparedStatement query);

  <R> Future<R> withTransaction(Supplier<Future<R>> sup);
}
