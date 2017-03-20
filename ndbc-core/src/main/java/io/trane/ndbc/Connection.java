package io.trane.ndbc;

import java.util.function.Function;

import io.trane.future.Future;

public interface Connection {

	Future<QueryResult> executeStatement(String query, Object... params);
	
	<T> Future<T> inTransaction(Function<Connection, Future<T>> function);
}
