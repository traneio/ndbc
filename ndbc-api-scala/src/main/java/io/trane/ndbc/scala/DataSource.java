package io.trane.ndbc.scala;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import io.trane.ndbc.Config;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import scala.concurrent.Promise$;

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

  public static DataSource<PreparedStatement, Row> fromJdbcUrl(final String url) {
    return create(io.trane.ndbc.DataSource.fromJdbcUrl(url));
  }

  public static DataSource<PreparedStatement, Row> fromConfig(final Config config) {
    return create(io.trane.ndbc.DataSource.fromConfig(config));
  }

  public static <P extends PreparedStatement, R extends Row> DataSource<P, R> create(
      final io.trane.ndbc.DataSource<P, R> ds) {
    return new DataSource<>(ds);
  }

  private final io.trane.ndbc.DataSource<P, R> underlying;

  protected DataSource(final io.trane.ndbc.DataSource<P, R> underlying) {
    this.underlying = underlying;
  }

  protected final <T> Future<T> convert(final io.trane.future.Future<T> future) {
    final Promise<T> promise = Promise$.MODULE$.apply();
    future.onSuccess(promise::success).onFailure(promise::failure);
    return promise.future();
  }

  public final Future<List<R>> query(final String query) {
    return convert(underlying.query(query));
  }

  public final Future<Long> execute(final String statement) {
    return convert(underlying.execute(statement));
  }

  public final Future<List<R>> query(final P query) {
    return convert(underlying.query(query));
  }

  public final Future<Long> execute(final P statement) {
    return convert(underlying.execute(statement));
  }

  public final TransactionalDataSource<P, R> transactional() {
    return new TransactionalDataSource<>(underlying.transactional());
  }

  public final Future<Void> close() {
    return convert(underlying.close());
  }

  public final Config config() {
    return underlying.config();
  }
}