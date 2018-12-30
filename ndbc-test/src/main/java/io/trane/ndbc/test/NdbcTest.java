package io.trane.ndbc.test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

@RunWith(Parameterized.class)
public class NdbcTest<PS extends PreparedStatement, R extends Row> {

  @Parameter(0)
  public DataSource<PS, R> ds;
}
