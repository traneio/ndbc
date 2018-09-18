package io.trane.ndbc.test;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import io.trane.ndbc.DataSource;

@RunWith(Parameterized.class)
public class NdbcTest {

  @Parameter(0)
  public DataSource ds;

  @Parameter(1)
  public String label;
}
