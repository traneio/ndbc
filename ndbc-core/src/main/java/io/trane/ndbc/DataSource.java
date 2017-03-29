package io.trane.ndbc;

import java.util.function.Supplier;

import io.trane.future.Future;

public interface DataSource {

  Future<ResultSet> query(String query);

  Future<Integer> execute(String statement);

  Future<ResultSet> query(PreparedStatement query);

  Future<Integer> execute(PreparedStatement statement);

  <T> Future<T> transactional(Supplier<Future<T>> supplier);
}
