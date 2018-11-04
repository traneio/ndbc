package io.trane.ndbc.twitter;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import com.twitter.util.Future;
import com.twitter.util.Local;
import com.twitter.util.Promise;

import io.trane.ndbc.Config;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class DataSource<P extends PreparedStatement, R extends Row> {

  public static DataSource<PreparedStatement, Row> fromSystemProperties(final String prefix) {
    return create(io.trane.ndbc.DataSource.fromSystemProperties(prefix));
  }

  public static DataSource<PreparedStatement, Row> fromPropertiesFile(final String prefix, final String fileName)
      throws IOException {
    return create(io.trane.ndbc.DataSource.fromPropertiesFile(prefix, fileName));
  }

  public static DataSource<PreparedStatement, Row> fromProperties(final String prefix, final Properties properties) {
    return create(io.trane.ndbc.DataSource.fromProperties(prefix, properties));
  }

  public static DataSource<PreparedStatement, Row> fromConfig(final Config config) {
    return create(io.trane.ndbc.DataSource.fromConfig(config));
  }

  public static <P extends PreparedStatement, R extends Row> DataSource<P, R> create(
      final io.trane.ndbc.DataSource<P, R> ds) {
    return new DataSource<>(ds);
  }

  private final io.trane.ndbc.DataSource<P, R>       underlying;
  private final Local<TransactionalDataSource<P, R>> currentTransaction = new Local<>();

  private final io.trane.ndbc.DataSource<P, R> current() {
    return currentTransaction.apply().getOrElse(() -> underlying);
  }

  protected DataSource(final io.trane.ndbc.DataSource<P, R> underlying) {
    this.underlying = underlying;
  }

  protected final <T> Future<T> convert(final io.trane.future.Future<T> future) {
    final Promise<T> promise = Promise.apply();
    promise.setInterruptHandler(new PartialFunction<Throwable, BoxedUnit>() {

      @Override
      public BoxedUnit apply(Throwable v1) {
        future.raise(v1);
        return BoxedUnit.UNIT;
      }

      @Override
      public boolean isDefinedAt(Throwable x) {
        return true;
      }
    });
    future.onSuccess(promise::setValue).onFailure(promise::setException);
    return promise;
  }

  public final Future<List<R>> query(final String query) {
    return convert(current().query(query));
  }

  public final Future<Long> execute(final String statement) {
    return convert(current().execute(statement));
  }

  public final Future<List<R>> query(final P query) {
    return convert(current().query(query));
  }

  public final Future<Long> execute(final P statement) {
    return convert(current().execute(statement));
  }

  public final TransactionalDataSource<P, R> transactional() {
    return new TransactionalDataSource<>(current().transactional());
  }

  public final <T> Future<T> transactional(final Supplier<Future<T>> supplier) {
    if (currentTransaction.apply().nonEmpty())
      return supplier.get();
    else
      return currentTransaction.let(transactional(), () -> supplier.get());
  }

  public final Future<Void> close() {
    return convert(current().close());
  }

  public final Config config() {
    return current().config();
  }
}