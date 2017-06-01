package io.trane.ndbc;

import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.future.Transformer;

public interface Connection {

  Future<Boolean> isValid();

  Future<Void> close();

  Future<ResultSet> query(String query);

  Future<Integer> execute(String query);

  Future<ResultSet> query(PreparedStatement query);

  Future<Integer> execute(PreparedStatement query);

  default <R> Future<R> withTransaction(Supplier<Future<R>> sup) {
    return execute("BEGIN")
        .flatMap(v -> sup.get())
        .transformWith(new Transformer<R, Future<R>>() {
          @Override
          public Future<R> onException(Throwable ex) {
            return execute("ROLLBACK").flatMap(v -> Future.exception(ex));
          }

          @Override
          public Future<R> onValue(R value) {
            return execute("COMMIT").map(v -> value);
          }
        });
  }
}
