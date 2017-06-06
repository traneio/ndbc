package io.trane.ndbc.benchmark;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicLong;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import io.trane.future.Responder;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;

public class PostgresNetty4Benchmark {

  private final Config config = Config
      .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 5432, "postgres").password("postgres")
      .poolValidationInterval(Duration.ofSeconds(1)).poolMaxSize(10);

  protected DataSource ds = DataSource.fromConfig(config);
  private final Duration timeout = Duration.ofMillis(100);

  public static void main(String[] args) throws CheckedFutureException {
    new PostgresNetty4Benchmark().test();
  }

  private final AtomicLong ok = new AtomicLong(0);
  private final AtomicLong nok = new AtomicLong(0);
  ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
  PreparedStatement q = PreparedStatement.apply("SELECT * FROM test_benchmark");
  private final long start = System.currentTimeMillis();

  private final Responder<ResultSet> resp = new Responder<ResultSet>() {

    @Override
    public void onValue(ResultSet arg0) {
      ok.incrementAndGet();
    }

    @Override
    public void onException(Throwable arg0) {
      arg0.printStackTrace();
      nok.incrementAndGet();
    }
  };

  private void test() throws CheckedFutureException {
    ds.execute("DROP TABLE IF EXISTS test_benchmark").get(timeout);
    ds.execute("CREATE TABLE test_benchmark (v varchar)").get(timeout);
    ds.execute("INSERT INTO test_benchmark VALUES ('s')").get(timeout);

    monitor();

    for (int i = 0; i < 4; i++)
      loop();
  }

  private Future<ResultSet> loop() {
    return ds.query(q).respond(resp).flatMap(v -> loop());
  }

  private Future<Void> monitor() {
    return Future.delay(Duration.ofSeconds(1), scheduler).flatMap(v -> {
      long time = System.currentTimeMillis() - start;
      System.out.println("ok " + ok.get() / time);
      System.out.println("nok " + nok.get() / time);
      return monitor();
    });
  }

}
