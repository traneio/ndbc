package io.trane.ndbc.postgres.netty4;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import io.trane.ndbc.DataSource;

public class PostgresTestRunner extends BlockJUnit4ClassRunner {

  public static DataSource ds;
  private String           testPrefix;

  public PostgresTestRunner(Class<?> klass) throws InitializationError {
    super(klass);
  }

  @Override
  public void run(RunNotifier notifier) {
    super.run(notifier);
  }

  @Override
  protected String getName() {
    return "TEST " + super.getName();
  }

  @Override
  protected Object createTest() throws Exception {
    Object test = super.createTest();
    return test;
  }

}
