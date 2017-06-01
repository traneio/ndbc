package io.trane.ndbc;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;

import io.trane.future.Future;

public interface DataSource {

  public static DataSource fromSystemProperties(String prefix) {
    return fromConfig(Config.fromSystemProperties(prefix));
  }

  public static DataSource fromPropertiesFile(String prefix, String fileName) throws IOException {
    return fromConfig(Config.fromPropertiesFile(prefix, fileName));
  }

  public static DataSource fromProperties(String prefix, Properties properties) {
    return fromConfig(Config.fromProperties(prefix, properties));
  }

  @SuppressWarnings("unchecked")
  public static DataSource fromConfig(Config config) {
    try {
      Supplier<DataSource> supplier = (Supplier<DataSource>) Class.forName(config.dataSourceSupplierClass)
          .getConstructor(Config.class).newInstance(config);
      return supplier.get();
    } catch (Exception e) {
      throw new RuntimeException("Can't load DataSource supplier: " + config.dataSourceSupplierClass, e);
    }
  }

  Future<ResultSet> query(String query);

  Future<Integer> execute(String statement);

  Future<ResultSet> query(PreparedStatement query);

  Future<Integer> execute(PreparedStatement statement);

  <T> Future<T> transactional(Supplier<Future<T>> supplier);

  Future<Void> close();
}
