package io.trane.ndbc;

import java.util.Optional;
import java.util.Properties;

public class ConfigError extends RuntimeException {
  private static final long serialVersionUID = -5455508571025077170L;

  final Properties properties;
  final String prefix;
  final String name;
  final Optional<Exception> cause;

  public ConfigError(Properties properties, String prefix, String name, Optional<Exception> cause) {
    super("Invalid config `" + prefix + "." + name + "`." + cause.map(e -> "Cause: " + e.toString()).orElse(""));
    this.properties = properties;
    this.prefix = prefix;
    this.name = name;
    this.cause = cause;
    cause.map(this::initCause);
  }
}
