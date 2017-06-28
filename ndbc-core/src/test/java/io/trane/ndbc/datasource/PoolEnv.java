package io.trane.ndbc.datasource;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Supplier;

import org.junit.After;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public class PoolEnv {

  final int maxSize = 10;
  final int maxWaiters = 10;
  final Duration validationInterval = Duration.ofSeconds(1);
  final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

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
    public Future<List<Row>> query(String query) {
      return notExpected();
    }

    @Override
    public Future<Integer> execute(String query) {
      return notExpected();
    }

    @Override
    public Future<List<Row>> query(PreparedStatement query) {
      return notExpected();
    }

    @Override
    public Future<Integer> execute(PreparedStatement query) {
      return notExpected();
    }

    @Override
    public <R> Future<R> withTransaction(Supplier<Future<R>> sup) {
      return notExpected();
    }
  };

}
