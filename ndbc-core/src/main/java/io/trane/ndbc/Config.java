package io.trane.ndbc;

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

public final class Config {

  public static final Config fromSystemProperties(final String prefix) {
    return fromProperties(prefix, System.getProperties());
  }

  public static Config fromPropertiesFile(final String prefix, final String file) throws IOException {
    final Properties properties = new Properties();
    final FileInputStream fis = new FileInputStream(file);
    properties.load(fis);
    fis.close();
    return fromProperties(prefix, properties);
  }

  public static final Config fromProperties(final String prefix, final Properties properties) {

    final String dataSourceSupplierClass = getRequiredProperty(prefix, properties, "dataSourceSupplierClass");
    final String host = getRequiredProperty(prefix, properties, "host");
    final int port = getRequiredProperty(prefix, properties, "port", Integer::parseInt);
    final String user = getRequiredProperty(prefix, properties, "user");

    Config config = Config.apply(dataSourceSupplierClass, host, port, user);

    config = config
        .charset(getProperty(prefix, properties, "charset", Charset::forName).orElse(Charset.defaultCharset()));
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

  public static final Config apply(final String dataSourceSupplierClass, final String host, final int port,
      final String user) {
    return new Config(dataSourceSupplierClass, host, port, user, Charset.defaultCharset(), Optional.empty(),
        Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
  }

  private static final <T> T getRequiredProperty(final String prefix, final Properties properties, final String name,
      final Function<String, T> parser) {
    try {
      return parser.apply(getRequiredProperty(prefix, properties, name));
    } catch (final Exception ex) {
      throw new ConfigError(properties, prefix, name, Optional.of(ex));
    }
  }

  private static final String getRequiredProperty(final String prefix, final Properties properties, final String name) {
    return getProperty(prefix, properties, name).orElseGet(() -> {
      throw new ConfigError(properties, prefix, name, Optional.empty());
    });
  }

  private static final <T> Optional<T> getProperty(final String prefix, final Properties properties, final String name,
      final Function<String, T> parser) {
    try {
      return getProperty(prefix, properties, name).map(parser);
    } catch (final Exception ex) {
      throw new ConfigError(properties, prefix, name, Optional.of(ex));
    }
  }

  private static final Optional<String> getProperty(final String prefix, final Properties properties,
      final String name) {
    return Optional.ofNullable(properties.getProperty(prefix + "." + name));
  }

  public final String                dataSourceSupplierClass;
  public final String                host;
  public final int                   port;
  public final String                user;
  public final Charset               charset;
  public final Optional<String>      password;
  public final Optional<String>      database;
  public final Optional<Integer>     poolMaxSize;
  public final Optional<Integer>     poolMaxWaiters;
  public final Optional<Duration>    poolValidationInterval;
  public final Optional<Set<String>> encodingClasses;
  public final Optional<Integer>     nioThreads;

  private Config(final String dataSourceSupplierClass, final String host, final int port, final String user,
      final Charset charset, final Optional<String> password, final Optional<String> database,
      final Optional<Integer> poolMaxSize, final Optional<Integer> poolMaxWaiters,
      final Optional<Duration> poolValidationInterval, final Optional<Set<String>> encodingClasses,
      final Optional<Integer> nioThreads) {
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
    this.encodingClasses = encodingClasses.map(Collections::unmodifiableSet);
    this.nioThreads = nioThreads;
  }

  public final Config charset(final Charset charset) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, password, database, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses, nioThreads);
  }

  public final Config password(final String password) {
    return password(Optional.of(password));
  }

  public final Config password(final Optional<String> password) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, password, database, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses, nioThreads);
  }

  public final Config database(final String database) {
    return database(Optional.of(database));
  }

  public final Config database(final Optional<String> database) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, password, database, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses, nioThreads);
  }

  public final Config poolMaxSize(final int poolMaxSize) {
    return poolMaxSize(Optional.of(poolMaxSize));
  }

  public final Config poolMaxSize(final Optional<Integer> poolMaxSize) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, password, database, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses, nioThreads);
  }

  public final Config poolMaxWaiters(final int poolMaxWaiters) {
    return poolMaxWaiters(Optional.of(poolMaxWaiters));
  }

  public final Config poolMaxWaiters(final Optional<Integer> poolMaxWaiters) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, password, database, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses, nioThreads);
  }

  public final Config poolValidationInterval(final Duration poolValidationInterval) {
    return poolValidationInterval(Optional.of(poolValidationInterval));
  }

  public final Config poolValidationInterval(final Optional<Duration> poolValidationInterval) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, password, database, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses, nioThreads);
  }

  public final Config encodingClasses(final Set<String> encodingClasses) {
    return encodingClasses(Optional.of(encodingClasses));
  }

  public final Config encodingClasses(final Optional<Set<String>> encodingClasses) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, password, database, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses, nioThreads);
  }

  public final Config addEncodingClass(final String encoding) {
    return encodingClasses(Optional.ofNullable(encoding).map(enc -> {
      final Set<String> encodingClasses = new HashSet<>();
      encodingClasses.addAll(encodingClasses);
      encodingClasses.add(enc);
      return encodingClasses;
    }));
  }

  public final Config nioThreads(final int nioThreads) {
    return nioThreads(Optional.of(nioThreads));
  }

  public final Config nioThreads(final Optional<Integer> nioThreads) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, password, database, poolMaxSize,
        poolMaxWaiters, poolValidationInterval, encodingClasses, nioThreads);
  }
}
