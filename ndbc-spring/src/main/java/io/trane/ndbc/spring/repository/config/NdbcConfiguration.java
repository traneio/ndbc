package io.trane.ndbc.spring.repository.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

@Configuration
public class NdbcConfiguration {

  Config config = Config.create("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 0, "user")
      .database("test_schema").password("test").embedded("io.trane.ndbc.postgres.embedded.EmbeddedSupplier");

  @Bean
  public DataSource<PreparedStatement, Row> dataSource() {
    return DataSource.fromConfig(config);
  }
}