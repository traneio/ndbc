package io.trane.ndbc;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Supplier;

import io.trane.future.Future;

public interface DataSource<P extends PreparedStatement, R extends Row> {

  public static DataSource<PreparedStatement, Row> fromSystemProperties(final String prefix) {
    return fromConfig(Config.fromSystemProperties(prefix));
  }

  public static DataSource<PreparedStatement, Row> fromPropertiesFile(final String prefix, final String fileName)
      throws IOException {
    return fromConfig(Config.fromPropertiesFile(prefix, fileName));
  }

  public static DataSource<PreparedStatement, Row> fromProperties(final String prefix, final Properties properties) {
    return fromConfig(Config.fromProperties(prefix, properties));
  }

  public static DataSource<PreparedStatement, Row> fromJdbcUrl(final String url) {
    return fromConfig(Config.fromJdbcUrl(url));
  }

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

  Future<List<R>> query(String query);

  Future<Long> execute(String statement);

  Future<List<R>> query(P query);

  Future<Long> execute(P statement);

  <T> Future<T> transactional(Supplier<Future<T>> supplier);

  TransactionalDataSource<P, R> transactional();

  Future<Void> close();

  Config config();
}
