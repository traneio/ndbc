package io.trane.ndbc;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import io.trane.future.Future;

public interface DataSource {

  public static DataSource fromSystemProperties(final String prefix) {
    return fromConfig(Config.fromSystemProperties(prefix));
  }

  public static DataSource fromPropertiesFile(final String prefix, final String fileName) throws IOException {
    return fromConfig(Config.fromPropertiesFile(prefix, fileName));
  }

  public static DataSource fromProperties(final String prefix, final Properties properties) {
    return fromConfig(Config.fromProperties(prefix, properties));
  }

  @SuppressWarnings("unchecked")
  public static DataSource fromConfig(final Config config) {
    try {
      final Supplier<DataSource> supplier = (Supplier<DataSource>) Class.forName(config.dataSourceSupplierClass())
          .getConstructor(Config.class).newInstance(config);
      return supplier.get();
    } catch (final Exception e) {
      throw new RuntimeException("Can't load DataSource supplier: " + config.dataSourceSupplierClass(), e);
    }
  }

  Future<List<Row>> query(String query);

  Future<Integer> execute(String statement);

  Future<List<Row>> query(PreparedStatement query);

  Future<Integer> execute(PreparedStatement statement);

  <T> Future<T> transactional(Supplier<Future<T>> supplier);

  Future<Void> close();
}
