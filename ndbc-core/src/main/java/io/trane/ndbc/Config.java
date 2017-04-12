package io.trane.ndbc;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

public class Config {

  public static Config fromProperties(String prefix, Properties properties) {
    String dataSourceSupplierClass = getRequiredProperty(prefix, properties, "dataSourceSupplierClass");
    Charset charset = Charset.forName(getProperty(prefix, properties, "charset").orElse("UTF-8"));
    String user = getRequiredProperty(prefix, properties, "user");
    Optional<String> password = getProperty(prefix, properties, "password");
    Optional<String> database = getProperty(prefix, properties, "database");
    String host = getRequiredProperty(prefix, properties, "host");
    int port = getProperty(prefix, properties, "port", Integer::parseInt).orElse(5432);
    int poolMaxSize = getProperty(prefix, properties, "poolMaxSize", Integer::parseInt).orElse(Integer.MAX_VALUE);
    int poolMaxWaiters = getProperty(prefix, properties, "poolMaxWaiters", Integer::parseInt).orElse(Integer.MAX_VALUE);
    Duration poolValidationInterval = getProperty(prefix, properties, "poolValidationIntervalSeconds", Long::parseLong)
        .map(Duration::ofSeconds).orElse(Duration.ofMillis(Long.MAX_VALUE));
    return new Config(dataSourceSupplierClass, charset, user, password, database, host, port, poolMaxSize,
        poolMaxWaiters, poolValidationInterval);
  }

  private static String getRequiredProperty(String prefix, Properties properties, String name) {
    return getProperty(prefix, properties, name).orElseGet(() -> {
      throw new ConfigError(properties, prefix, name, Optional.empty());
    });
  }

  private static <T> Optional<T> getProperty(String prefix, Properties properties, String name,
      Function<String, T> parser) {
    try {
      return getProperty(prefix, properties, name).map(parser);
    } catch (Exception ex) {
      throw new ConfigError(properties, prefix, name, Optional.of(ex));
    }
  }

  private static Optional<String> getProperty(String prefix, Properties properties, String name) {
    return Optional.ofNullable(properties.getProperty(prefix + "." + name));
  }

  public final String dataSourceSupplierClass;
  public final Charset charset;
  public final String user;
  public final Optional<String> password;
  public final Optional<String> database;
  public final String host;
  public final int port;
  public final int poolMaxSize;
  public final int poolMaxWaiters;
  public final Duration poolValidationInterval;

  public Config(String dataSourceSupplierClass, Charset charset, String user, Optional<String> password,
      Optional<String> database, String host, int port, int poolMaxSize, int poolMaxWaiters,
      Duration poolValidationInterval) {
    super();
    this.dataSourceSupplierClass = dataSourceSupplierClass;
    this.charset = charset;
    this.user = user;
    this.password = password;
    this.database = database;
    this.host = host;
    this.port = port;
    this.poolMaxSize = poolMaxSize;
    this.poolMaxWaiters = poolMaxWaiters;
    this.poolValidationInterval = poolValidationInterval;
  }
}
