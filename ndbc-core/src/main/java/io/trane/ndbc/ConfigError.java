package io.trane.ndbc;

import java.util.Optional;
import java.util.Properties;

public class ConfigError extends RuntimeException {
  private static final long serialVersionUID = -5455508571025077170L;

  private final Properties properties;
  private final String prefix;
  private final String name;

  public ConfigError(Properties properties, String prefix, String name, Optional<Exception> cause) {
    super("");
    this.properties = properties;
    this.prefix = prefix;
    this.name = name;
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
}
