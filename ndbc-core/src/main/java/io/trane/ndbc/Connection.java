package io.trane.ndbc;

import java.util.function.Function;

import io.trane.future.Future;

public interface Connection {

	Future<QueryResult> query(String query);

	Future<Boolean> execute(String query);

	Future<Boolean> executeBatch(String query);

	<T> Future<T> inTransaction(Function<Connection, Future<T>> function);
}
