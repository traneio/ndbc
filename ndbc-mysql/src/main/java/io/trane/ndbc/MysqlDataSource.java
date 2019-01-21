package io.trane.ndbc;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import io.trane.future.Future;
import io.trane.ndbc.flow.Flow;

/**
 * A mysql-specific data source. It currently has the same features as the
 * generic data source, but in the future it might contain mysql-specific APIs.
 */
public class MysqlDataSource implements DataSource<MysqlPreparedStatement, MysqlRow> {

  /**
   * Creates the data source based on system properties. See
   * `Config.fromSystemProperties` for more details.
   * 
   * @param prefix
   *          the configuration prefix
   * @return the data source instance
   */
  public static MysqlDataSource fromSystemProperties(final String prefix) {
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
  public static MysqlDataSource fromPropertiesFile(final String prefix,
      final String fileName)
      throws IOException {
    return create(io.trane.ndbc.DataSource.fromPropertiesFile(prefix,
        fileName));
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
  public static MysqlDataSource fromProperties(final String prefix, final Properties properties) {
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
  public static MysqlDataSource fromJdbcUrl(final String url) {
    return create(io.trane.ndbc.DataSource.fromJdbcUrl(url));
  }

  /**
   * Creates the data source based on a `Config` instance.
   * 
   * @param config
   *          the config
   * @return the data source instance
   */
  public static MysqlDataSource fromConfig(final Config config) {
    return create(DataSource.fromConfig(config));
  }

  /**
   * Creates a completion stage data source based on a regular data source
   * 
   * @param ds
   *          the regular data source
   * @return the new data source
   */
  public static MysqlDataSource create(final DataSource<PreparedStatement, Row> ds) {
    return new MysqlDataSource(ds);
  }

  private final DataSource<PreparedStatement, Row> underlying;

  protected MysqlDataSource(final DataSource<PreparedStatement, Row> underlying) {
    this.underlying = underlying;
  }

  @Override
  public Future<List<MysqlRow>> query(final String query) {
    return conv(underlying.query(query));
  }

  @Override
  public Future<Long> execute(final String statement) {
    return underlying.execute(statement);
  }

  @Override
  public Future<List<MysqlRow>> query(final MysqlPreparedStatement query) {
    return conv(underlying.query(query));
  }

  @Override
  public Flow<MysqlRow> stream(MysqlPreparedStatement query) {
    return underlying.stream(query).map(MysqlRow::create);
  }

  @Override
  public Future<Long> execute(final MysqlPreparedStatement statement) {
    return underlying.execute(statement);
  }

  @Override
  public <T> Future<T> transactional(final Supplier<Future<T>> supplier) {
    return underlying.transactional(supplier);
  }

  @Override
  public TransactionalDataSource<MysqlPreparedStatement, MysqlRow> transactional() {
    return new TransactionalDataSource<MysqlPreparedStatement, MysqlRow>() {

      private final TransactionalDataSource<PreparedStatement, Row> underlying = MysqlDataSource.this.underlying
          .transactional();

      @Override
      public TransactionalDataSource<MysqlPreparedStatement, MysqlRow> transactional() {
        return this;
      }

      @Override
      public <T> Future<T> transactional(final Supplier<Future<T>> supplier) {
        return underlying.transactional(supplier);
      }

      @Override
      public Future<List<MysqlRow>> query(final MysqlPreparedStatement query) {
        return conv(underlying.query(query));
      }

      @Override
      public Future<List<MysqlRow>> query(final String query) {
        return conv(underlying.query(query));
      }

      @Override
      public Flow<MysqlRow> stream(MysqlPreparedStatement query) {
        return underlying.stream(query).map(MysqlRow::create);
      }

      @Override
      public Future<Long> execute(final MysqlPreparedStatement statement) {
        return underlying.execute(statement);
      }

      @Override
      public Future<Long> execute(final String statement) {
        return underlying.execute(statement);
      }

      @Override
      public Config config() {
        return underlying.config();
      }

      @Override
      public Future<Void> close() {
        return underlying.close();
      }

      @Override
      public Future<Void> rollback() {
        return underlying.rollback();
      }

      @Override
      public Future<Void> commit() {
        return underlying.commit();
      }
    };
  }

  @Override
  public Future<Void> close() {
    return underlying.close();
  }

  @Override
  public Config config() {
    return underlying.config();
  }

  private final Future<List<MysqlRow>> conv(final Future<List<Row>> f) {
    return f.map(l -> l.stream().map(MysqlRow::create).collect(Collectors.toList()));
  }
}
