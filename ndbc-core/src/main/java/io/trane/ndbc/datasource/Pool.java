package io.trane.ndbc.datasource;

import io.trane.future.Future;

public interface Pool<T extends Connection> {

  Future<T> acquire();

  void release(T c);

  Future<Void> close();
}