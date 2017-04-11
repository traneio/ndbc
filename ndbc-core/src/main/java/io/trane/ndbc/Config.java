package io.trane.ndbc;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

public class Config {
  
  public static Config fromProperties(Properties properties) {
    return null; // TODO
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
