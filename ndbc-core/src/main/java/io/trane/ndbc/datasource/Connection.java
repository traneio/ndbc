package io.trane.ndbc.datasource;

import java.util.List;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.flow.Flow;

public interface Connection {

  Future<Boolean> isValid();

  Future<Void> close();

  Future<List<Row>> query(String query);

  Future<Long> execute(String query);

  Future<List<Row>> query(PreparedStatement query);

  Flow<Row> stream(PreparedStatement query);

  Future<Long> execute(PreparedStatement query);

  Future<Void> beginTransaction();

  Future<Void> commit();

  Future<Void> rollback();
}
