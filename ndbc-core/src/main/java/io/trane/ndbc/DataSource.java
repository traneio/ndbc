package io.trane.ndbc;

import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;

public interface DataSource<C extends Connection> {

  Future<ResultSet> query(String query);

  Future<Integer> execute(String statement);

  Future<ResultSet> query(PreparedStatement query);

  Future<Integer> execute(PreparedStatement statement);

  <T> Future<T> transactional(Supplier<Future<T>> supplier);

  <T> Future<T> withConnection(Function<C, Future<T>> supplier);
}
