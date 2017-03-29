package io.trane.ndbc.postgres.netty4;

import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;

public class DataSource implements io.trane.ndbc.DataSource {

  @Override
  public Future<ResultSet> query(String query) {
    return null;
  }

  @Override
  public Future<Integer> execute(String query) {
    return null;
  }

  @Override
  public Future<ResultSet> query(PreparedStatement query) {
    return null;
  }

  @Override
  public Future<Integer> execute(PreparedStatement query) {
    return null;
  }

  @Override
  public <T> Future<T> transactional(Supplier<Future<T>> supplier) {
    return null;
  }

}
