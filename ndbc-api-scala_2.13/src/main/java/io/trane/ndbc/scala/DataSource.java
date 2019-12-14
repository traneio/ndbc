package io.trane.ndbc.scala;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import io.trane.future.scala.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

/**
 * A data source that returns traneio's Scala Futures.
 * 
 * @param <P>
 *          the type of prepared statements
 * @param <R>
 *          the type of result rows
 */
public class DataSource<P extends PreparedStatement, R extends Row> {

  /**
   * Creates the data source based on system properties. See
   * `Config.fromSystemProperties` for more details.
   * 
   * @param prefix
   *          the configuration prefix
   * @return the data source instance
   */
  public static DataSource<PreparedStatement, Row> fromSystemProperties(final String prefix) {
    return create(io.trane.ndbc.DataSource.fromSystemProperties(prefix));
  }

  /**
   * Creates the data source based on a properties file. See
   * `Config.fromPropertiesFile` for more details.
   * 
   * @param prefix
   *          the configuration prefix
   * @param fileName
   *          the properties file path
   * @return the data source instance
   * @throws IOException
   *           if the file can't read
   */
  public static DataSource<PreparedStatement, Row> fromPropertiesFile(final String prefix, final String fileName)
      throws IOException {
    return create(io.trane.ndbc.DataSource.fromPropertiesFile(prefix, fileName));
  }

  /**
   * Creates the data source based on a `Properties` object. See
   * `Config.fromProperties` for more details.
   * 
   * @param prefix
   *          the configuration prefix
   * @param properties
   *          the properties object
   * @return the data source instance
   */
  public static DataSource<PreparedStatement, Row> fromProperties(final String prefix, final Properties properties) {
    return create(io.trane.ndbc.DataSource.fromProperties(prefix, properties));
  }

  /**
   * Creates the data source based on a JDBC url. See `Config.fromJdbcUrl` for
   * more details.
   * 
   * @param url
   *          the JDBC url
   * @return the data source instance
   */
  public static DataSource<PreparedStatement, Row> fromJdbcUrl(final String url) {
    return create(io.trane.ndbc.DataSource.fromJdbcUrl(url));
  }

  /**
   * Creates the data source based on a `Config` instance.
   * 
   * @param config
   *          the config
   * @return the data source instance
   */
  public static DataSource<PreparedStatement, Row> fromConfig(final Config config) {
    return create(io.trane.ndbc.DataSource.fromConfig(config));
  }

  /**
   * Creates a scala future data source based on a regular data source
   * 
   * @param ds
   *          the regular data source
   * @return the new data source
   */
  public static <P extends PreparedStatement, R extends Row> DataSource<P, R> create(
      final io.trane.ndbc.DataSource<P, R> ds) {
    return new DataSource<>(ds);
  }

  private final io.trane.ndbc.DataSource<P, R> underlying;

  protected DataSource(final io.trane.ndbc.DataSource<P, R> underlying) {
    this.underlying = underlying;
  }

  protected final <T> Future<T> convert(final io.trane.future.Future<T> future) {
    return new Future<>(future);
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

  public final <T> Future<T> transactional(Supplier<Future<T>> supplier) {
    return convert(underlying.transactional(() -> supplier.get().underlying()));
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