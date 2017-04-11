package io.trane.ndbc;

import java.util.Properties;
import java.util.function.Function;
import java.util.function.Supplier;

import io.trane.future.Future;

public interface DataSource {

  public static DataSource create() {
    return create(System.getProperties());
  }

  public static DataSource create(Properties properties) {
    return create(Config.fromProperties(properties));
  }

  @SuppressWarnings("unchecked")
  public static DataSource create(Config config) {
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

  <T> Future<T> withConnection(Function<Connection, Future<T>> supplier);
}
