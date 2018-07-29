package io.trane.ndbc.datasource;

import java.util.function.Function;

import io.trane.future.Future;

public interface Pool<T extends Connection> {

	<R> Future<R> apply(Function<T, Future<R>> f);

	Future<Void> close();
}