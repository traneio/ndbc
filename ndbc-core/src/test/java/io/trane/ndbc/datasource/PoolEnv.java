package io.trane.ndbc.datasource;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.After;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.flow.Flow;

public class PoolEnv {

  final int                      maxSize            = 10;
  final int                      maxWaiters         = 10;
  final Duration                 validationInterval = Duration.ofSeconds(1);
  final ScheduledExecutorService scheduler          = Executors.newScheduledThreadPool(1);

  @After
  public void shutdown() {
    scheduler.shutdown();
  }

  protected Connection conn() {
    return new TestConnection();
  }

  class TestConnection implements Connection {

    private <T> T notExpected() {
      throw new IllegalStateException("Unpexted call");
    }

    @Override
    public Future<Boolean> isValid() {
      return notExpected();
    }

    @Override
    public Future<Void> close() {
      return notExpected();
    }

    @Override
    public Future<List<Row>> query(final String query) {
      return notExpected();
    }

    @Override
    public Future<Long> execute(final String query) {
      return notExpected();
    }

    @Override
    public Future<List<Row>> query(final PreparedStatement query) {
      return notExpected();
    }

    @Override
    public Future<Long> execute(final PreparedStatement query) {
      return notExpected();
    }

    @Override
    public Future<Void> beginTransaction() {
      return notExpected();
    }

    @Override
    public Future<Void> commit() {
      return notExpected();
    }

    @Override
    public Future<Void> rollback() {
      return notExpected();
    }

    @Override
    public Flow<Row> stream(PreparedStatement query) {
      return notExpected();
    }
  };

  class TransactionalTestConnection extends TestConnection {

    public int begin    = 0;
    public int commit   = 0;
    public int rollback = 0;

    @Override
    public Future<Void> beginTransaction() {
      begin++;
      return Future.VOID;
    }

    @Override
    public Future<Void> commit() {
      commit++;
      return Future.VOID;
    }

    @Override
    public Future<Void> rollback() {
      rollback++;
      return Future.VOID;
    }
  };
}
