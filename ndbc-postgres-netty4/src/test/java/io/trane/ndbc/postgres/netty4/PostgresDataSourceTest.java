package io.trane.ndbc.postgres.netty4;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.reactivestreams.tck.PublisherVerification;
import org.reactivestreams.tck.TestEnvironment;

import io.trane.future.Future;
import io.trane.ndbc.NdbcException;
import io.trane.ndbc.PostgresPreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.test.DataSourceTest;

public class PostgresDataSourceTest extends DataSourceTest {

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return PostgresEnv.dataSources;
  }

  public PostgresDataSourceTest() {
    super("varchar");
  }

  @Test(expected = NdbcException.class)
  public void cancellation() throws Throwable {
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    try {
      final Future<List<Row>> f = ds.query("SELECT pg_sleep(999)");
      f.raise(new Exception(""));
      f.get(timeout);
    } finally {
      scheduler.shutdown();
    }
  }

  private static class TestSubscriber implements Subscriber<Row> {

    Subscription s = null;

    @Override
    public void onSubscribe(Subscription s) {
      this.s = s;
    }

    @Override
    public void onNext(Row t) {
    }

    @Override
    public void onError(Throwable t) {
    }

    @Override
    public void onComplete() {
    }

  }

  @Test
  public void stream() {
    Publisher<Row> p = ds.stream(PostgresPreparedStatement.create("SELECT generate_series(0, 1000)"));
    new PublisherVerification<Row>(new TestEnvironment(true)) {

      @Override
      public Publisher<Row> createPublisher(long elements) {
        return p;
      }

      @Override
      public Publisher<Row> createFailedPublisher() {
        return null;
      }
    };
  }
}
