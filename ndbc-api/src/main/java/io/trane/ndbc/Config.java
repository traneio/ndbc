package io.trane.ndbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
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

/**
 * This class holds the information needed to create an NDBC DataSource.
 * `Config` objects are immutable and return new instances when a new parameter
 * is set.
 */
public final class Config {

  /**
   * SSL configuration
   */
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

    /**
     * Creates a new SSL configuration without a root certificate file
     * 
     * @param mode
     *          the SSL mode
     * @return the SSL configuration
     */
    public static final SSL create(final Mode mode) {
      return new SSL(mode, Optional.empty());
    }

    /**
     * Creates a new SSL configuration with a root certificate file
     * 
     * @param mode
     *          the SSL mode
     * @param rootCert
     *          file with the root certificate
     * @return the SSL configuration
     */
    public static final SSL create(final Mode mode, final File rootCert) {
      return new SSL(mode, Optional.of(rootCert));
    }

    private final Mode           mode;
    private final Optional<File> rootCert;

    private SSL(final Mode mode, final Optional<File> rootCert) {
      this.mode = mode;
      this.rootCert = rootCert;
    }

    /**
     * @return the SSL mode
     */
    public final Mode mode() {
      return mode;
    }

    /**
     * @return the optional root certificate file
     */
    public final Optional<File> rootCert() {
      return rootCert;
    }

    /**
     * @param file
     *          the root certificate file
     * @return a new SSL configuration with the specified root certificate file
     */
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

  /**
   * Configuration class for embedded databases
   */
  public static class Embedded {
    public final String           supplierClass;
    public final Optional<String> version;

    /**
     * Creates an embedded database configuration using the provided supplier
     * class. The class must:
     *
     * 1. Have a constructor with parameters `(Config config, Optional<String>
     * version)`
     * 
     * 2. Implement the interface `Supplier<DataSource<PreparedStatement, Row>>`
     * and produce a `DataSource` backed by an embedded database.
     * 
     * See the modules with the `-embedded` suffix for the default
     * implementations.
     * 
     * @param supplierClass
     *          the embedded database supplier class
     * @return the embedded database configuration
     */
    public static Embedded create(final String supplierClass) {
      return create(supplierClass, Optional.empty());
    }

    /**
     * Creates an embedded database configuration for a specific version. See
     * `create(final String supplierClass)` for more information about the
     * supplier class. The version parameter can be used by the supplier class
     * to choose a specific version of the database.
     * 
     * @param supplierClass
     *          the embedded database supplier class
     * @param version
     *          the embedded database version
     * @return the embedded database configuration
     */
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

  /**
   * Creates a configuration based on system properties with the specified
   * prefix. For instance, if the prefix is `myDB`, there should be a system
   * property `myDB.user` defined.
   * 
   * See `fromProperties(final String prefix, final Properties properties)` for
   * details on the available configurations.
   * 
   * @param prefix
   *          the configuration prefix
   * @return the configuration
   */
  public static final Config fromSystemProperties(final String prefix) {
    return fromProperties(prefix, System.getProperties());
  }

  /**
   * Creates a configuration based on properties file with the specified prefix.
   * For instance, if the prefix is `myDB`, there should be a property
   * `myDB.user` defined.
   * 
   * See https://en.wikipedia.org/wiki/.properties for the file format.
   * 
   * See `fromProperties(final String prefix, final Properties properties)` for
   * details on the available configurations.
   * 
   * @param prefix
   *          the configuration prefix
   * @param file
   *          the full path to the properties file
   * @return the configuration
   * @throws IOException
   */
  public static Config fromPropertiesFile(final String prefix, final String file) throws IOException {
    final Properties properties = new Properties();
    final FileInputStream fis = new FileInputStream(file);
    properties.load(fis);
    fis.close();
    return fromProperties(prefix, properties);
  }

  /**
   * Creates a configuration based on `Properties` object with the specified
   * prefix. For instance, if the prefix is `myDB`, there should be a property
   * `myDB.user` defined.
   * 
   * Required configurations:
   * 
   * `dataSourceSupplierClass` -> A class that implements
   * `Supplier<DataSource<PreparedStatement, Row>>` and has a constructor with
   * one parameter `Config config`.
   * 
   * `host` -> The database host
   * 
   * `port` -> The database port
   * 
   * `user` -> The database user
   * 
   * Optional configurations:
   * 
   * `charset` -> the database charset to be parsed using
   * java.nio.charset.Charset.forname. Default: Charset.defaultCharset()
   * 
   * `password` -> The database password. Default: empty
   * 
   * `database` -> The database name (sometimes referred as schema). Default:
   * empty
   * 
   * `poolMaxSize` -> The maximum number of connections. Default: unlimited
   * 
   * `poolMaxWaiters` -> Sets the size of the wait queue for database
   * connections. After this limit, the pool will return failures if no new
   * connections can be established.
   * 
   * `poolValidationIntervalSeconds` -> If set, the connection pool will
   * validate connections using the specified interval. Default: no validation
   * 
   * `connectionTimeoutSeconds` -> If set, the pool will fail to acquire a
   * connection if it takes longer than the specified timeout. Default: no
   * timeout
   * 
   * `queryTimeoutSeconds` -> If set, the pool will fail to execute a query if
   * it takes longer than the specified timeout. Default: no timeout
   * 
   * `encodingClasses` -> Allows users to register a comma-separated list of
   * custom encoding classes. The classes must have an empty constructor and
   * extend the driver-specific `Encoding` class.
   * 
   * `ssl.mode` -> Sets the SSL mode. See Config.Ssl.Mode for details. Default:
   * empty
   * 
   * `ssl.rootCert` -> The path to the root certificate file. This configuration
   * is valid only if `ssl.mode` is set. Default: empty
   * 
   * `embedded.supplierClass` -> If set, the data source will be backed by an
   * embedded database. See `Config.Embedded` for details. Default: empty
   * 
   * `embedded.version` -> Sets the embedded database version. Only valid if
   * `embedded.supplierClass` is defined. See `Config.Embedded` for more
   * details.
   * 
   * @param prefix
   *          the configuration prefix
   * @param properties
   *          the properties object
   * @return the configuration
   */
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

  /**
   * Creates the configuration based on a standard JDBC url.
   * 
   * @param url
   *          the JDBC url
   * @return the configuration
   */
  public static final Config fromJdbcUrl(final String url) {

    final Properties prop = new Properties();
    final String prefix = "url.";

    final URI uri = URI.create(url.replaceFirst("jdbc:", ""));

    String dsSupplier;
    final String scheme = uri.getScheme();
    if (scheme.contains("mysql"))
      dsSupplier = "io.trane.ndbc.mysql.netty4.DataSourceSupplier";
    else if (scheme.contains("postgres"))
      dsSupplier = "io.trane.ndbc.postgres.netty4.DataSourceSupplier";
    else
      throw new NdbcException("Can't determine the data source supplier from the jdbc url");
    prop.put(prefix + "dataSourceSupplierClass", dsSupplier);

    if (uri.getUserInfo() != null) {
      final String[] split = uri.getUserInfo().split(":");
      prop.put(prefix + "user", split[0]);
      if (split.length > 1)
        prop.put(prefix + "password", split[1]);
    }

    if (uri.getHost() != null)
      prop.put(prefix + "host", uri.getHost());

    if (uri.getPort() >= 0)
      prop.put(prefix + "port", "" + uri.getPort());

    if (uri.getPath() != null) {
      final String norm = uri.getPath().replaceFirst("/", "");
      if (!norm.isEmpty())
        prop.put(prefix + "database", norm);
    }

    if (uri.getQuery() != null) {
      final String[] params = uri.getQuery().split("&");
      for (final String param : params) {
        final String[] split = param.split("=");
        String key;
        try {
          key = URLDecoder.decode(split[0], "UTF-8");
          String value;
          if (split.length > 1)
            value = URLDecoder.decode(split[1], "UTF-8");
          else
            value = "";
          prop.put(prefix + key, value);
        } catch (final UnsupportedEncodingException e) {
          throw new NdbcException("Can't decode the url parameters", e);
        }
      }
    }

    return fromProperties(prefix.substring(0, prefix.length() - 1), prop);
  }

  /**
   * Creates a configuration with the specified parameters
   * 
   * @param dataSourceSupplierClass
   *          the supplier class
   * @param host
   *          the database host
   * @param port
   *          the database port
   * @param user
   *          the database user
   * @return the configuration
   */
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

  public final Config embedded(final String supplierClass) {
    return embedded(supplierClass, Optional.empty());
  }

  public final Config embedded(final String supplierClass, final Optional<String> version) {
    return embedded(Embedded.create(supplierClass, version));
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
