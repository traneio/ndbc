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

  public static DataSource apply(io.trane.ndbc.DataSource ds) {
    return new DataSource(ds);
  }

  private final io.trane.ndbc.DataSource underlying;

  protected DataSource(io.trane.ndbc.DataSource underlying) {
    this.underlying = underlying;
  }

  protected final <T> CompletionStage<T> conv(Future<T> fut) {
    CompletableFuture<T> cf = new CompletableFuture<>();
    fut.onSuccess(cf::complete).onFailure(cf::completeExceptionally);
    return cf;
  }

  public final CompletionStage<List<Row>> query(String query) {
    return conv(underlying.query(query));
  }

  public final CompletionStage<Long> execute(String statement) {
    return conv(underlying.execute(statement));
  }

  public final CompletionStage<List<Row>> query(PreparedStatement query) {
    return conv(underlying.query(query));
  }

  public final CompletionStage<Long> execute(PreparedStatement statement) {
    return conv(underlying.execute(statement));
  }

  public final TransactionalDataSource transactional() {
    return new TransactionalDataSource(underlying.transactional());
  }

  public final CompletionStage<Void> close() {
    return conv(underlying.close());
  }

  public final Config config() {
    return underlying.config();
  }
}
