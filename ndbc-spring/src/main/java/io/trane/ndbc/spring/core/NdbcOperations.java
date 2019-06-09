package io.trane.ndbc.spring.core;

import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public class NdbcOperations {

  private final DataSource<PreparedStatement, Row> dataSource;

  public NdbcOperations(final DataSource<PreparedStatement, Row> dataSource) {
    this.dataSource = dataSource;
  }
}
