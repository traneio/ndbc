package io.trane.ndbc.postgres.netty4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public class DataSourceTest extends TestEnv {

  @Before
  public void recreateSchema() throws CheckedFutureException {
    ds.execute("DROP TABLE IF EXISTS test").get(timeout);
    ds.execute("CREATE TABLE test (s varchar)").get(timeout);
    ds.execute("INSERT INTO test VALUES ('s')").get(timeout);
  }

  @After
  public void close() throws CheckedFutureException {
    ds.close().get(timeout);
  }

  @Test
  public void simpleQuery() throws CheckedFutureException {
    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();

    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedQueryNoParams() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("SELECT * FROM test");

    final Iterator<Row> rows = ds.query(ps).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedQueryWithParams() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("SELECT * FROM test WHERE s = ?").bind("s");

    final Iterator<Row> rows = ds.query(ps).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteInsert() throws CheckedFutureException {
    ds.execute("INSERT INTO test VALUES ('u')");

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteUpdate() throws CheckedFutureException {
    ds.execute("UPDATE test SET s = 'u'");

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteDelete() throws CheckedFutureException {
    ds.execute("DELETE FROM test");

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteInsertNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("INSERT INTO test VALUES ('u')");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteUpdateNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("UPDATE test SET s = 'u'");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteDeleteNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("DELETE FROM test WHERE s = 's'");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteInsertWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("INSERT INTO test VALUES (?)").bind("u");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteUpdateWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("UPDATE test SET s = ?").bind("u");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteDeleteWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("DELETE FROM test WHERE s = ?").bind("s");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test(expected = CheckedFutureException.class)
  public void cancellation() throws CheckedFutureException {
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    try {
      final Future<Integer> f = ds.execute("SELECT pg_sleep(999)");
      f.raise(new RuntimeException());
      f.get(timeout);
    } finally {
      scheduler.shutdown();
    }
  }
}
