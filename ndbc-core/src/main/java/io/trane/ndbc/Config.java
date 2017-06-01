package io.trane.ndbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Config {

  public static Config fromSystemProperties(String prefix) {
    return fromProperties(prefix, System.getProperties());
  }

  public static Config fromPropertiesFile(String prefix, String file) throws IOException {
    Properties properties = new Properties();
    FileInputStream fis = new FileInputStream(file);
    properties.load(fis);
    fis.close();
    return fromProperties(prefix, properties);
  }

  public static Config fromProperties(String prefix, Properties properties) {

    String dataSourceSupplierClass = getRequiredProperty(prefix, properties, "dataSourceSupplierClass");
    String host = getRequiredProperty(prefix, properties, "host");
    int port = getRequiredProperty(prefix, properties, "port", Integer::parseInt);
    String user = getRequiredProperty(prefix, properties, "user");

    Config config = Config.apply(dataSourceSupplierClass, host, port, user);

    config = config.charset(getProperty(prefix, properties, "charset", Charset::forName));
    config = config.password(getProperty(prefix, properties, "password"));
    config = config.database(getProperty(prefix, properties, "database"));
    config = config.poolMaxSize(getProperty(prefix, properties, "poolMaxSize", Integer::parseInt));
    config = config.poolMaxWaiters(getProperty(prefix, properties, "poolMaxWaiters", Integer::parseInt));

    config = config.poolValidationInterval(
        getProperty(prefix, properties, "poolValidationIntervalSeconds", s -> Duration.ofSeconds(Long.parseLong(s))));

    config = config.encodingClasses(getProperty(prefix, properties, "encodingClasses")
        .map(k -> Stream.of(k.split(",")).collect(Collectors.toSet())));

    return config;
  }

  public static final Config apply(String dataSourceSupplierClass, String host, int port, String user) {
    return new Config(dataSourceSupplierClass, Charset.defaultCharset(), user, Optional.empty(), Optional.empty(), host,
        port, Integer.MAX_VALUE, Integer.MAX_VALUE, Duration.ofMillis(Long.MAX_VALUE), new HashSet<>());
  }

  private static <T> T getRequiredProperty(String prefix, Properties properties, String name,
      Function<String, T> parser) {
    try {
      return parser.apply(getRequiredProperty(prefix, properties, name));
    } catch (Exception ex) {
      throw new ConfigError(properties, prefix, name, Optional.of(ex));
    }
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
  public final Set<String> encodingClasses;

  private Config(String dataSourceSupplierClass, Charset charset, String user, Optional<String> password,
      Optional<String> database, String host, int port, int poolMaxSize, int poolMaxWaiters,
      Duration poolValidationInterval, Set<String> encodingClasses) {
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
    this.encodingClasses = Collections.unmodifiableSet(encodingClasses);
  }

  public final Config charset(Charset charset) {
    return new Config(dataSourceSupplierClass, charset, user, password, database, host, port, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses);
  }

  public final Config charset(Optional<Charset> charset) {
    return charset.map(this::charset).orElse(this);
  }

  public final Config password(String password) {
    return new Config(dataSourceSupplierClass, charset, user, Optional.ofNullable(password), database, host, port,
        poolMaxSize, poolMaxWaiters, poolValidationInterval, encodingClasses);
  }

  public final Config password(Optional<String> password) {
    return password.map(this::password).orElse(this);
  }

  public final Config database(String database) {
    return new Config(dataSourceSupplierClass, charset, user, password, Optional.of(database), host, port, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses);
  }

  public final Config database(Optional<String> database) {
    return database.map(this::database).orElse(this);
  }

  public final Config poolMaxSize(int poolMaxSize) {
    return new Config(dataSourceSupplierClass, charset, user, password, database, host, port, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses);
  }

  public final Config poolMaxSize(Optional<Integer> poolMaxSize) {
    return poolMaxSize.map(this::poolMaxSize).orElse(this);
  }

  public final Config poolMaxWaiters(int poolMaxWaiters) {
    return new Config(dataSourceSupplierClass, charset, user, password, database, host, port, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses);
  }

  public final Config poolMaxWaiters(Optional<Integer> poolMaxWaiters) {
    return poolMaxWaiters.map(this::poolMaxWaiters).orElse(this);
  }

  public final Config poolValidationInterval(Duration poolValidationInterval) {
    return new Config(dataSourceSupplierClass, charset, user, password, database, host, port, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses);
  }

  public final Config poolValidationInterval(Optional<Duration> poolValidationInterval) {
    return poolValidationInterval.map(this::poolValidationInterval).orElse(this);
  }

  public final Config encodingClasses(Set<String> encodingClasses) {
    return new Config(dataSourceSupplierClass, charset, user, password, database, host, port, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, Collections.unmodifiableSet(encodingClasses));
  }

  public final Config encodingClasses(Optional<Set<String>> encodingClasses) {
    return encodingClasses.map(this::encodingClasses).orElse(this);
  }

  public final Config addEncodingClass(String encoding) {
    Set<String> encodingClasses = new HashSet<>();
    encodingClasses.addAll(encodingClasses);
    encodingClasses.add(encoding);
    return new Config(dataSourceSupplierClass, charset, user, password, database, host, port, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses);
  }
}
