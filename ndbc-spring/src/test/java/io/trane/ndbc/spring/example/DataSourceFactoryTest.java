package io.trane.ndbc.spring.example;

import java.time.Duration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AsyncCustomerConfig.class)
public class DataSourceFactoryTest {

  @Autowired
  DataSource<PreparedStatement, Row> datasource;

  @Test
  public void createDataSource() throws CheckedFutureException {
    System.out.println(datasource);
    final Iterable<Row> rows = datasource.query("SELECT 1").get(Duration.ofSeconds(3));

    rows.forEach(x -> System.out.println(x.getInteger(0)));
  }
}
