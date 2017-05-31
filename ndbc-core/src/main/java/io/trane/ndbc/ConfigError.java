package io.trane.ndbc;

import java.util.Optional;
import java.util.Properties;

public class ConfigError extends RuntimeException {
  private static final long serialVersionUID = -5455508571025077170L;

  private final Properties properties;
  private final String prefix;
  private final String name;
  private final Optional<Exception> cause;

  public ConfigError(Properties properties, String prefix, String name, Optional<Exception> cause) {
    super("");
    this.properties = properties;
    this.prefix = prefix;
    this.name = name;
    this.cause = cause;
  }

  public Properties getProperties() {
    return properties;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getName() {
    return name;
  }
  
  @Override
  public String toString() {
    return "Invalid config `" + prefix + "." + name + "`." + cause.map(e -> "Cause: " + e.toString()).orElse("");
  }
}
