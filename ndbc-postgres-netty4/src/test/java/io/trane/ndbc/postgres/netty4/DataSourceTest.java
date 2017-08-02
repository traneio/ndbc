package io.trane.ndbc.postgres.netty4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

  private static int   tableSuffix = 1;

  private final String table       = "table_" + tableSuffix++;

  @Before
  public void recreateSchema() throws CheckedFutureException {
    ds.execute("DROP TABLE IF EXISTS " + table).get(timeout);
    ds.execute("CREATE TABLE " + table + " (s varchar)").get(timeout);
    ds.execute("INSERT INTO " + table + " VALUES ('s')").get(timeout);
  }

  @After
  public void close() throws CheckedFutureException {
    ds.close().get(timeout);
  }
  
//  @Test
//  public void array() throws CheckedFutureException {
//    final Iterator<Row> rows = ds.query(PreparedStatement.apply("SELECT ARRAY[1, 2, 3]")).get(timeout).iterator();
//
////    assertEquals(rows.next().column(0).getString(), "s");
////    assertFalse(rows.hasNext());
//  }

  @Test
  public void simpleQuery() throws CheckedFutureException {
    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();

    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedQueryNoParams() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("SELECT * FROM " + table);

    final Iterator<Row> rows = ds.query(ps).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedQueryWithParams() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("SELECT * FROM " + table + " WHERE s = ?")
        .setString("s");

    final Iterator<Row> rows = ds.query(ps).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteInsert() throws CheckedFutureException {
    ds.execute("INSERT INTO " + table + " VALUES ('u')").get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteUpdate() throws CheckedFutureException {
    ds.execute("UPDATE " + table + " SET s = 'u'").get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteDelete() throws CheckedFutureException {
    ds.execute("DELETE FROM " + table).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteInsertNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("INSERT INTO " + table + " VALUES ('u')");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteUpdateNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("UPDATE " + table + " SET s = 'u'");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteDeleteNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("DELETE FROM " + table + " WHERE s = 's'");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteInsertWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("INSERT INTO " + table + " VALUES (?)")
        .setString("u");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteUpdateWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("UPDATE " + table + " SET s = ?")
        .setString("u");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteDeleteWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("DELETE FROM " + table + " WHERE s = ?")
        .setString("s");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void transactionSuccess() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("DELETE FROM " + table + " WHERE s = ?")
        .setString("s");

    ds.transactional(() -> ds.execute(ps)).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void transactionLocalFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("DELETE FROM " + table + " WHERE s = ?")
        .setString("s");

    ds.transactional(() -> ds.execute(ps).map(v -> {
      throw new IllegalStateException();
    })).join(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertTrue(rows.hasNext());
  }
  
  @Test
  public void transactionDBFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.apply("DELETE FROM INVALID_TABLE WHERE s = ?")
        .setString("s");

    ds.transactional(() -> ds.execute(ps)).join(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertTrue(rows.hasNext());
  }

  @Test(expected = RuntimeException.class)
  public void cancellation() throws CheckedFutureException {
    final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    try {
      final Future<Long> f = ds.execute("SELECT pg_sleep(999)");
      f.raise(new RuntimeException());
      f.get(timeout);
    } finally {
      scheduler.shutdown();
    }
  }
}
