package io.trane.ndbc.scala;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import io.trane.ndbc.Config;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import scala.concurrent.Future;
import scala.concurrent.Promise;

public class DataSource {

  public static DataSource fromSystemProperties(final String prefix) {
    return apply(io.trane.ndbc.DataSource.fromSystemProperties(prefix));
  }

  public static DataSource fromPropertiesFile(final String prefix, final String fileName) throws IOException {
    return apply(io.trane.ndbc.DataSource.fromPropertiesFile(prefix, fileName));
  }

  public static DataSource fromProperties(final String prefix, final Properties properties) {
    return apply(io.trane.ndbc.DataSource.fromProperties(prefix, properties));
  }

  public static DataSource fromConfig(final Config config) {
    return apply(io.trane.ndbc.DataSource.fromConfig(config));
  }

  public static DataSource apply(final io.trane.ndbc.DataSource ds) {
    return new DataSource(ds);
  }

  private final io.trane.ndbc.DataSource underlying;

  protected DataSource(final io.trane.ndbc.DataSource underlying) {
    this.underlying = underlying;
  }

  protected final <T> Future<T> convert(final io.trane.future.Future<T> future) {
    final Promise<T> promise = Promise.apply();
    future.onSuccess(promise::success).onFailure(promise::failure);
    return promise.future();
  }

  public final Future<List<Row>> query(final String query) {
    return this.convert(underlying.query(query));
  }

  public final Future<Long> execute(final String statement) {
    return convert(underlying.execute(statement));
  }

  public final Future<List<Row>> query(final PreparedStatement query) {
    return convert(underlying.query(query));
  }

  public final Future<Long> execute(final PreparedStatement statement) {
    return convert(underlying.execute(statement));
  }

  public final TransactionalDataSource transactional() {
    return new TransactionalDataSource(underlying.transactional());
  }

  public final Future<Void> close() {
    return convert(underlying.close());
  }

  public final Config config() {
    return underlying.config();
  }
}