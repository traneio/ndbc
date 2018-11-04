package io.trane.ndbc.completionStage;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

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

  private final io.trane.ndbc.DataSource<P, R> underlying;

  protected DataSource(final io.trane.ndbc.DataSource<P, R> underlying) {
    this.underlying = underlying;
  }

  protected final <T> CompletionStage<T> conv(final Future<T> fut) {
    final CompletableFuture<T> cf = new CompletableFuture<>();
    fut.onSuccess(cf::complete).onFailure(cf::completeExceptionally);
    return cf;
  }

  public final CompletionStage<List<R>> query(final String query) {
    return conv(underlying.query(query));
  }

  public final CompletionStage<Long> execute(final String statement) {
    return conv(underlying.execute(statement));
  }

  public final CompletionStage<List<R>> query(final P query) {
    return conv(underlying.query(query));
  }

  public final CompletionStage<Long> execute(final P statement) {
    return conv(underlying.execute(statement));
  }

  public final TransactionalDataSource<P, R> transactional() {
    return new TransactionalDataSource<>(underlying.transactional());
  }

  public final CompletionStage<Void> close() {
    return conv(underlying.close());
  }

  public final Config config() {
    return underlying.config();
  }
}
