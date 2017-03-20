package io.trane.ndbc;

import java.util.function.Function;

import io.trane.future.Future;

public interface Connection {

	Future<Connection> connect();
	
	Future<Connection> disconnect();
	
	Future<Boolean> isConnected();
	
	Future<QueryResult> executeStatement(String query, Object... params);
	
	<T> Future<T> inTransaction(Function<Connection, Future<T>> function);
}
