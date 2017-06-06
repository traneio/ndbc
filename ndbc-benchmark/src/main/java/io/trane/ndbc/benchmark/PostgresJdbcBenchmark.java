package io.trane.ndbc.benchmark;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class PostgresJdbcBenchmark {

  public static void main(String[] args) throws SQLException {
    Properties props = new Properties();
    props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource");
    props.setProperty("dataSource.user", "postgres");
    props.setProperty("dataSource.password", "postgres");
    props.setProperty("dataSource.databaseName", "postgres");
    props.put("dataSource.logWriter", new PrintWriter(System.out));

    HikariConfig config = new HikariConfig(props);
    HikariDataSource ds = new HikariDataSource(config);

    ExecutorService executor = Executors.newCachedThreadPool();

    AtomicLong ok = new AtomicLong(0);
    AtomicLong nok = new AtomicLong(0);

    Connection c = ds.getConnection();
    c.createStatement().execute("DROP TABLE IF EXISTS test_benchmark");
    c.createStatement().execute("CREATE TABLE test_benchmark (v varchar)");
    c.createStatement().execute("INSERT INTO test_benchmark VALUES ('s')");
    c.close();

    long start = System.currentTimeMillis();

    // monitor
    executor.submit(() -> {
      while (true) {
        long time = System.currentTimeMillis() - start;
        System.out.println("ok " + ok.get() / time);
        System.out.println("nok " + nok.get() / time);
        Thread.sleep(1000);
      }
    });

    for (int i = 0; i < 4; i++)
      executor.submit(() -> {
        while (true) {
          Connection conn = ds.getConnection();
          try {
            conn.prepareStatement("SELECT * FROM test_benchmark").executeQuery();
            ok.incrementAndGet();
          } catch (Exception e) {
            e.printStackTrace();
            nok.incrementAndGet();
          } finally {
            conn.close();
          }
        }
      });
  }

}
