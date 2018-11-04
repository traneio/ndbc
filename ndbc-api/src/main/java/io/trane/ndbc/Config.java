package io.trane.ndbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Config {

  public static class SSL {
    public static enum Mode {
      /**
       * only try a non-SSL connection
       */
      DISABLE,
      /**
       * first try an SSL connection; if that fails, try a non-SSL connection
       */
      PREFER,
      /**
       * only try an SSL connection, but don't verify Certificate Authority
       */
      REQUIRE,
      /**
       * only try an SSL connection, and verify that the server certificate is
       * issued by a trusted certificate authority (CA)
       */
      VERIFY_CA,
      /**
       * only try an SSL connection, verify that the server certificate is
       * issued by a trusted CA and that the server host name matches that in
       * the certificate
       */
      VERIFY_FULL
    }

    public static final SSL create(final Mode mode) {
      return new SSL(mode, Optional.empty());
    }

    public static final SSL create(final Mode mode, final File rootCert) {
      return new SSL(mode, Optional.of(rootCert));
    }

    private final Mode           mode;
    private final Optional<File> rootCert;

    private SSL(final Mode mode, final Optional<File> rootCert) {
      this.mode = mode;
      this.rootCert = rootCert;
    }

    public final Mode mode() {
      return mode;
    }

    public final Optional<File> rootCert() {
      return rootCert;
    }

    public final SSL rootCert(final File file) {
      return new SSL(mode, Optional.ofNullable(file));
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((mode == null) ? 0 : mode.hashCode());
      result = (prime * result) + ((rootCert == null) ? 0 : rootCert.hashCode());
      return result;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final SSL other = (SSL) obj;
      if (mode != other.mode)
        return false;
      if (rootCert == null) {
        if (other.rootCert != null)
          return false;
      } else if (!rootCert.equals(other.rootCert))
        return false;
      return true;
    }
  }

  public static class Embedded {
    public final String           supplierClass;
    public final Optional<String> version;

    public static Embedded create(final String supplierClass, final Optional<String> version) {
      return new Embedded(supplierClass, version);
    }

    private Embedded(final String supplierClass, final Optional<String> version) {
      this.supplierClass = supplierClass;
      this.version = version;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((supplierClass == null) ? 0 : supplierClass.hashCode());
      result = (prime * result) + ((version == null) ? 0 : version.hashCode());
      return result;
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      final Embedded other = (Embedded) obj;
      if (supplierClass == null) {
        if (other.supplierClass != null)
          return false;
      } else if (!supplierClass.equals(other.supplierClass))
        return false;
      if (version == null) {
        if (other.version != null)
          return false;
      } else if (!version.equals(other.version))
        return false;
      return true;
    }

    @Override
    public String toString() {
      return "Embedded [supplierClass=" + supplierClass + ", version=" + version + "]";
    }
  }

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

    Config config = Config.create(dataSourceSupplierClass, host, port, user);

    config = config.charset(getProperty(prefix, properties, "charset", Charset::forName)
        .orElse(Charset.defaultCharset()));

    config = config.password(getProperty(prefix, properties, "password"));
    config = config.database(getProperty(prefix, properties, "database"));
    config = config.poolMaxSize(getProperty(prefix, properties, "poolMaxSize", Integer::parseInt));
    config = config.poolMaxWaiters(getProperty(prefix, properties, "poolMaxWaiters", Integer::parseInt));

    config = config.poolValidationInterval(getProperty(prefix, properties, "poolValidationIntervalSeconds",
        s -> Duration.ofSeconds(Long.parseLong(s))));

    config = config.connectionTimeout(getProperty(prefix, properties, "connectionTimeoutSeconds",
        s -> Duration.ofSeconds(Long.parseLong(s))));

    config = config.queryTimeout(getProperty(prefix, properties, "queryTimeoutSeconds",
        s -> Duration.ofSeconds(Long.parseLong(s))));

    config = config.encodingClasses(getProperty(prefix, properties, "encodingClasses")
        .map(k -> Stream.of(k.split(",")).filter(s -> !s.isEmpty()).collect(Collectors.toSet())));

    config = config.ssl(getProperty(prefix, properties, "ssl.mode", SSL.Mode::valueOf).map(sslMode -> {
      final SSL ssl = SSL.create(sslMode);
      return getProperty(prefix, properties, "ssl.rootCert").map(rootCert -> ssl.rootCert(new File(rootCert)))
          .orElse(ssl);
    }));

    config = config.embedded(getProperty(prefix, properties, "embedded.supplierClass")
        .map(cls -> Embedded.create(cls, getProperty(prefix, properties, "embedded.version"))));

    return config;
  }

  public static final Config create(final String dataSourceSupplierClass, final String host,
      final int port, final String user) {

    final AtomicInteger threadNumber = new AtomicInteger(0);
    final ThreadFactory daemonFactory = r -> {
      final Thread t = Executors.defaultThreadFactory().newThread(r);
      t.setName("ndbc-scheduler-" + threadNumber.incrementAndGet());
      t.setDaemon(true);
      return t;
    };

    final ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1, daemonFactory);

    return new Config(dataSourceSupplierClass, host, port, user, Charset.defaultCharset(),
        scheduler, Optional.empty(),
        Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
        Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
  }

  private static final <T> T getRequiredProperty(final String prefix, final Properties properties, final String name,
      final Function<String, T> parser) {
    final String value = getRequiredProperty(prefix, properties, name);
    try {
      return parser.apply(value);
    } catch (final Exception ex) {
      throw new NdbcException("Can't parse value `" + value + "` for config `" + prefix + "." + name + "`.",
          ex);
    }
  }

  private static final String getRequiredProperty(final String prefix, final Properties properties,
      final String name) {
    return getProperty(prefix, properties, name).orElseGet(() -> {
      throw new NdbcException("Missing config `" + prefix + "." + name + "`.");
    });
  }

  private static final <T> Optional<T> getProperty(final String prefix, final Properties properties,
      final String name, final Function<String, T> parser) {
    return getProperty(prefix, properties, name).map(value -> {
      try {
        return parser.apply(value);
      } catch (final Exception ex) {
        throw new NdbcException(
            "Can't parse value `" + value + "` for config `" + prefix + "." + name + "`.", ex);
      }
    });
  }

  private static final Optional<String> getProperty(final String prefix, final Properties properties,
      final String name) {
    return Optional.ofNullable(properties.getProperty(prefix + "." + name));
  }

  private final String                   dataSourceSupplierClass;
  private final String                   host;
  private final int                      port;
  private final String                   user;
  private final Charset                  charset;
  private final ScheduledExecutorService scheduler;
  private final Optional<String>         password;
  private final Optional<String>         database;
  private final Optional<Integer>        poolMaxSize;
  private final Optional<Integer>        poolMaxWaiters;
  private final Optional<Duration>       connectionTimeout;
  private final Optional<Duration>       queryTimeout;
  private final Optional<Duration>       poolValidationInterval;
  private final Optional<Set<String>>    encodingClasses;
  private final Optional<Integer>        nioThreads;
  private final Optional<SSL>            ssl;
  private final Optional<Embedded>       embedded;

  private Config(final String dataSourceSupplierClass, final String host, final int port, final String user,
      final Charset charset, final ScheduledExecutorService scheduler, final Optional<String> password,
      final Optional<String> database, final Optional<Integer> poolMaxSize, final Optional<Integer> poolMaxWaiters,
      final Optional<Duration> connectionTimeout, final Optional<Duration> queryTimeout,
      final Optional<Duration> poolValidationInterval, final Optional<Set<String>> encodingClasses,
      final Optional<Integer> nioThreads, final Optional<SSL> ssl, final Optional<Embedded> embeddedDatabase) {
    this.dataSourceSupplierClass = dataSourceSupplierClass;
    this.charset = charset;
    this.scheduler = scheduler;
    this.user = user;
    this.password = password;
    this.database = database;
    this.host = host;
    this.port = port;
    this.poolMaxSize = poolMaxSize;
    this.poolMaxWaiters = poolMaxWaiters;
    this.connectionTimeout = connectionTimeout;
    this.queryTimeout = queryTimeout;
    this.poolValidationInterval = poolValidationInterval;
    this.encodingClasses = encodingClasses.map(Collections::unmodifiableSet);
    this.nioThreads = nioThreads;
    this.ssl = ssl;
    this.embedded = embeddedDatabase;
  }

  public final String dataSourceSupplierClass() {
    return dataSourceSupplierClass;
  }

  public final String host() {
    return host;
  }

  public final int port() {
    return port;
  }

  public final Config port(final int port) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final String user() {
    return user;
  }

  public final Charset charset() {
    return charset;
  }

  public final Config charset(final Charset charset) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final ScheduledExecutorService scheduler() {
    return scheduler;
  }

  public final Config scheduler(final ScheduledExecutorService scheduler) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<String> password() {
    return password;
  }

  public final Config password(final String password) {
    return password(Optional.of(password));
  }

  public final Config password(final Optional<String> password) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<String> database() {
    return database;
  }

  public final Config database(final String database) {
    return database(Optional.of(database));
  }

  public final Config database(final Optional<String> database) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<Integer> poolMaxSize() {
    return poolMaxSize;
  }

  public final Config poolMaxSize(final int poolMaxSize) {
    return poolMaxSize(Optional.of(poolMaxSize));
  }

  public final Config poolMaxSize(final Optional<Integer> poolMaxSize) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<Integer> poolMaxWaiters() {
    return poolMaxWaiters;
  }

  public final Config poolMaxWaiters(final int poolMaxWaiters) {
    return poolMaxWaiters(Optional.of(poolMaxWaiters));
  }

  public final Config poolMaxWaiters(final Optional<Integer> poolMaxWaiters) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<Duration> connectionTimeout() {
    return connectionTimeout;
  }

  public final Config connectionTimeout(final Duration connectionTimeout) {
    return connectionTimeout(Optional.of(connectionTimeout));
  }

  public final Config connectionTimeout(final Optional<Duration> connectionTimeout) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<Duration> queryTimeout() {
    return queryTimeout;
  }

  public final Config queryTimeout(final Duration queryTimeout) {
    return queryTimeout(Optional.of(queryTimeout));
  }

  public final Config queryTimeout(final Optional<Duration> queryTimeout) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<Duration> poolValidationInterval() {
    return poolValidationInterval;
  }

  public final Config poolValidationInterval(final Duration poolValidationInterval) {
    return poolValidationInterval(Optional.of(poolValidationInterval));
  }

  public final Config poolValidationInterval(final Optional<Duration> poolValidationInterval) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<Set<String>> encodingClasses() {
    return encodingClasses;
  }

  public final Config encodingClasses(final Set<String> encodingClasses) {
    return encodingClasses(Optional.of(encodingClasses));
  }

  public final Config encodingClasses(final Optional<Set<String>> encodingClasses) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Config addEncodingClass(final String encodingClass) {
    return encodingClasses(Optional.ofNullable(encodingClass).map(enc -> {
      final Set<String> encodingClasses = new HashSet<>();
      this.encodingClasses.ifPresent(encodingClasses::addAll);
      encodingClasses.add(enc);
      return encodingClasses;
    }));
  }

  public final Optional<Integer> nioThreads() {
    return nioThreads;
  }

  public final Config nioThreads(final int nioThreads) {
    return nioThreads(Optional.of(nioThreads));
  }

  public final Config nioThreads(final Optional<Integer> nioThreads) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<SSL> ssl() {
    return ssl;
  }

  public final Config ssl(final SSL ssl) {
    return ssl(Optional.of(ssl));
  }

  public final Config ssl(final Optional<SSL> ssl) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final Optional<Embedded> embedded() {
    return embedded;
  }

  public final Config embedded(final Embedded embedded) {
    return embedded(Optional.of(embedded));
  }

  public final Config embedded(final Optional<Embedded> embedded) {
    return new Config(dataSourceSupplierClass, host, port, user, charset, scheduler, password, database,
        poolMaxSize, poolMaxWaiters, connectionTimeout, queryTimeout, poolValidationInterval, encodingClasses,
        nioThreads, ssl, embedded);
  }

  public final <T> Optional<List<T>> loadCustomEncodings() {
    return encodingClasses().map(l -> l.stream().map(n -> this.<T>loadEncoding(n)).collect(Collectors.toList()));
  }

  @SuppressWarnings("unchecked")
  private final <T> T loadEncoding(final String cls) {
    try {
      return (T) Class.forName(cls).newInstance();
    } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      throw new NdbcException("Can't load encoding " + cls + ". Make sure to provide an empty constructor.",
          e);
    }
  }

}
