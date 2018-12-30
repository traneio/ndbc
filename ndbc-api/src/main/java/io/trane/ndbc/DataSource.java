package io.trane.ndbc;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.flow.Flow;

/**
 * The generic datasource interface.
 *
 * @param <P>
 *          The prepared statement type.
 * @param <R>
 *          The result row type.
 */
public interface DataSource<P extends PreparedStatement, R extends Row> {

  /**
   * Creates the data source based on system properties. See
   * `Config.fromSystemProperties` for more details.
   * 
   * @param prefix
   *          the configuration prefix
   * @return the data source instance
   */
  public static DataSource<PreparedStatement, Row> fromSystemProperties(final String prefix) {
    return fromConfig(Config.fromSystemProperties(prefix));
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
    return fromConfig(Config.fromPropertiesFile(prefix, fileName));
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
    return fromConfig(Config.fromProperties(prefix, properties));
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
    return fromConfig(Config.fromJdbcUrl(url));
  }

  /**
   * Creates the data source based on a `Config` instance.
   * 
   * @param config
   *          the config
   * @return the data source instance
   */
  @SuppressWarnings("unchecked")
  public static DataSource<PreparedStatement, Row> fromConfig(final Config config) {
    return config.embedded().map(embedded -> {
      try {
        final Supplier<DataSource<PreparedStatement, Row>> supplier = (Supplier<DataSource<PreparedStatement, Row>>) Class
            .forName(embedded.supplierClass)
            .getConstructor(Config.class, Optional.class).newInstance(config, embedded.version);
        return supplier.get();
      } catch (final Exception e) {
        throw new NdbcException("Can't load DataSource supplier for: " + embedded, e);
      }
    }).orElseGet(() -> {
      try {
        final Supplier<DataSource<PreparedStatement, Row>> supplier = (Supplier<DataSource<PreparedStatement, Row>>) Class
            .forName(config.dataSourceSupplierClass())
            .getConstructor(Config.class).newInstance(config);
        return supplier.get();
      } catch (final Exception e) {
        throw new NdbcException("Can't load DataSource supplier: " + config.dataSourceSupplierClass(), e);
      }
    });
  }

  /**
   * Executes a query and returns a `Future` with the result rows.
   * 
   * @param query
   *          the query statement
   * @return the result rows
   */
  Future<List<R>> query(String query);

  /**
   * Executes a statement (DDLs, updates, inserts, etc).
   * 
   * @param statement
   *          the statement
   * @return a `Future` with the result. By default, it's the number of affected
   *         rows, but it can be a value if the statement has a `returning`
   *         clause.
   */
  Future<Long> execute(String statement);

  /**
   * Executes a prepared statement query.
   * 
   * @param query
   *          the prepared statement
   * @return a `Future` with the result rows.
   */
  Future<List<R>> query(P query);

  Flow<R> stream(P query);

  /**
   * Executes a prepared statement (DDLs, updates, inserts, etc).
   * 
   * @param statement
   *          the prepared statement
   * @return a `Future` with the result. By default, it's the number of affected
   *         rows, but it can be a value if the statement has a `returning`
   *         clause.
   */
  Future<Long> execute(P statement);

  /**
   * Executes a transactional block. Note that the transactional future should
   * be created within the supplier:
   * 
   * `dataSource.transactional(() -> doSomethingTransactional)`
   * 
   * The transaction is kept using a future `Local`, which is similar to a
   * `ThreadLocal` but for futures.
   * 
   * @param supplier
   *          the supplier to be executed under a transaction
   * @return
   */
  <T> Future<T> transactional(Supplier<Future<T>> supplier);

  /**
   * Returns a data source pinned to a transaction. The user needs to make sure
   * to call `TransactionalDataSource.close` once the transaction is done so the
   * connection can return to the pool.
   * 
   * @return the transactional data source
   */
  TransactionalDataSource<P, R> transactional();

  /**
   * Close this data source
   * 
   * @return a future that is fulfilled once the close operation is done
   */
  Future<Void> close();

  /**
   * Returns the config used to create the data source.
   * 
   * @return the config
   */
  Config config();
}
