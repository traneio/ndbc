package io.trane.ndbc;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.trane.future.Future;

/**
 * A postgres-specific data source. It provides custom prepared statements and
 * rows that allow users to access postgres-specific types like arrays.
 */
public class PostgresDataSource implements DataSource<PostgresPreparedStatement, PostgresRow> {

  /**
   * Creates the data source based on system properties. See
   * `Config.fromSystemProperties` for more details.
   * 
   * @param prefix
   *          the configuration prefix
   * @return the data source instance
   */
  public static PostgresDataSource fromSystemProperties(final String prefix) {
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
  public static PostgresDataSource fromPropertiesFile(final String prefix,
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
  public static PostgresDataSource fromProperties(final String prefix, final Properties properties) {
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
  public static PostgresDataSource fromJdbcUrl(final String url) {
    return create(io.trane.ndbc.DataSource.fromJdbcUrl(url));
  }

  /**
   * Creates the data source based on a `Config` instance.
   * 
   * @param config
   *          the config
   * @return the data source instance
   */
  public static PostgresDataSource fromConfig(final Config config) {
    return create(DataSource.fromConfig(config));
  }

  /**
   * Creates a completion stage data source based on a regular data source
   * 
   * @param ds
   *          the regular data source
   * @return the new data source
   */
  public static PostgresDataSource create(final DataSource<PreparedStatement, Row> ds) {
    return new PostgresDataSource(ds);
  }

  private final DataSource<PreparedStatement, Row> underlying;

  protected PostgresDataSource(final DataSource<PreparedStatement, Row> underlying) {
    this.underlying = underlying;
  }

  @Override
  public Future<List<PostgresRow>> query(final String query) {
    return conv(underlying.query(query));
  }

  @Override
  public Future<Long> execute(final String statement) {
    return underlying.execute(statement);
  }

  @Override
  public Future<List<PostgresRow>> query(final PostgresPreparedStatement query) {
    return conv(underlying.query(query));
  }

  @Override
  public Flow<PostgresRow> stream(PostgresPreparedStatement query) {
    Publisher<Row> p = underlying.stream(query);
    Publisher<PostgresRow> r = new Publisher<PostgresRow>() {

      @Override
      public void subscribe(Subscriber<? super PostgresRow> s) {
        // TODO Auto-generated method stub

      }

    };
    p.subscribe(new Subscriber<Row>() {
      @Override
      public void onSubscribe(Subscription s) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onNext(Row t) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onError(Throwable t) {
        // TODO Auto-generated method stub

      }

      @Override
      public void onComplete() {
        // TODO Auto-generated method stub

      }
    });
    return null;
  }

  @Override
  public Future<Long> execute(final PostgresPreparedStatement statement) {
    return underlying.execute(statement);
  }

  @Override
  public <T> Future<T> transactional(final Supplier<Future<T>> supplier) {
    return underlying.transactional(supplier);
  }

  @Override
  public TransactionalDataSource<PostgresPreparedStatement, PostgresRow> transactional() {
    return new TransactionalDataSource<PostgresPreparedStatement, PostgresRow>() {

      private final TransactionalDataSource<PreparedStatement, Row> underlying = PostgresDataSource.this.underlying
          .transactional();

      @Override
      public TransactionalDataSource<PostgresPreparedStatement, PostgresRow> transactional() {
        return this;
      }

      @Override
      public <T> Future<T> transactional(final Supplier<Future<T>> supplier) {
        return underlying.transactional(supplier);
      }

      @Override
      public Future<List<PostgresRow>> query(final PostgresPreparedStatement query) {
        return conv(underlying.query(query));
      }

      @Override
      public Future<List<PostgresRow>> query(final String query) {
        return conv(underlying.query(query));
      }

      @Override
      public Flow<PostgresRow> stream(PostgresPreparedStatement query) {
        return underlying.stream(query).map(PostgresRow::create);
      }

      @Override
      public Future<Long> execute(final PostgresPreparedStatement statement) {
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

  private final Future<List<PostgresRow>> conv(final Future<List<Row>> f) {
    return f.map(l -> l.stream().map(PostgresRow::create).collect(Collectors.toList()));
  }
}
