package io.trane.ndbc;

import java.util.function.Function;

import io.trane.future.Future;

public interface Connection {

  Future<QueryResult> query(String query);

  Future<Integer> execute(String query);
}
