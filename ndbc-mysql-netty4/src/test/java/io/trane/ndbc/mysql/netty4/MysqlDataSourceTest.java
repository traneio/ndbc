package io.trane.ndbc.mysql.netty4;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;

import io.trane.future.Future;
import io.trane.ndbc.NdbcException;
import io.trane.ndbc.Row;
import io.trane.ndbc.test.DataSourceTest;

public class MysqlDataSourceTest extends DataSourceTest {

  @Parameters(name = "{1}")
  public static Collection<Object[]> data() {
    return MysqlEnv.dataSources;
  }

  public MysqlDataSourceTest() {
    super("varchar(20)");
  }

  @Test
  public void cancellation() throws Throwable {
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    try {
      final long start = System.currentTimeMillis();
      final Future<List<Row>> f = ds.query("SELECT SLEEP(999)");
      f.raise(new NdbcException(""));
      f.get(timeout);
      assertTrue((System.currentTimeMillis() - start) < 999);
    } finally {
      scheduler.shutdown();
    }
  }

  @Override
  protected String streamQuery() {
    return "select (h*100+t*10+u+1) x from " +
        "(select 0 h union select 1 union select 2 union select 3 union select 4 union " +
        "select 5 union select 6 union select 7 union select 8 union select 9) A, " +
        "(select 0 t union select 1 union select 2 union select 3 union select 4 union " +
        "select 5 union select 6 union select 7 union select 8 union select 9) B, " +
        "(select 0 u union select 1 union select 2 union select 3 union select 4 union " +
        "select 5 union select 6 union select 7 union select 8 union select 9) C " +
        "order by x; ";
  }
}
