package io.trane.ndbc.mysql.netty4;

import java.time.Duration;

import io.trane.ndbc.Config;

public interface MysqlConfig {

  public static final Config instance = Config
      .apply("io.trane.ndbc.mysql.netty4.DataSourceSupplier", "localhost", 3306, "root")
      .password("root")
      .database("mysql")
      .poolValidationInterval(Duration.ofSeconds(1))
      .poolMaxSize(1)
      .poolMaxWaiters(0);

}
