package io.trane.ndbc.spring.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.SqlGeneratorSource;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.transaction.PlatformTransactionManager;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.spring.NdbcTransactionManager;
import io.trane.ndbc.spring.core.NdbcOperations;
import io.trane.ndbc.spring.repository.config.EnableNdbcRepositories;

@Configuration
@EnableNdbcRepositories
public class AsyncCustomerConfig {

  Config config = Config.create("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 0, "user")
      .database("test_schema").password("test").embedded("io.trane.ndbc.postgres.embedded.EmbeddedSupplier");

  @Bean
  SqlGeneratorSource generator() {
    return new SqlGeneratorSource(new RelationalMappingContext());
  }

  @Bean
  NdbcOperations operations() {
    return new NdbcOperations(dataSource());
  }

  @Bean
  PlatformTransactionManager transactionManager() {
    return new NdbcTransactionManager(dataSource());
  }

  @Bean
  public DataSource<PreparedStatement, Row> dataSource() {
    return DataSource.fromConfig(config);
  }

  @Bean
  public RelationalMappingContext jdbcMappingContext() {
    return new RelationalMappingContext();
  }
}