package io.trane.ndbc.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.trane.future.CheckedFutureException;
import io.trane.future.Promise;
import io.trane.ndbc.NdbcException;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.flow.Flow;

public abstract class DataSourceTest extends NdbcTest<PreparedStatement, Row> {

  protected Duration     timeout     = Duration.ofSeconds(999);

  private static int     tableSuffix = 1;

  protected final String table       = "table_" + tableSuffix++;

  private final String   stringColumnType;

  public DataSourceTest(final String stringColumnType) {
    this.stringColumnType = stringColumnType;
  }

  @Before
  public void recreateSchema() throws CheckedFutureException {
    ds.execute("DROP TABLE IF EXISTS " + table).get(timeout);
    ds.execute("CREATE TABLE " + table + " (s " + stringColumnType + ")").get(timeout);
    ds.execute("INSERT INTO " + table + " VALUES ('s')").get(timeout);
  }

  @Test
  public void simpleQuery() throws CheckedFutureException {
    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();

    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleQueryFailure() throws CheckedFutureException {
    try {
      ds.query("SELECT * FROM invalid_table").get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void simpleQueryInvalid() throws CheckedFutureException {
    try {
      ds.query("SLCT * FROM " + table).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
    }
  }

  @Test
  public void extendedQueryNoParams() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("SELECT * FROM " + table);

    final Iterator<Row> rows = ds.query(ps).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedQueryInvalid() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("SLCT * FROM " + table);
    try {
      ds.query(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
    }
  }

  @Test
  public void extendedQueryNoParamsFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("SELECT * FROM invalid_table");

    try {
      ds.query(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void extendedQueryWithParams() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("SELECT * FROM " + table + " WHERE s = ?").setString("s");

    final Iterator<Row> rows = ds.query(ps).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedQueryWithParamsFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("SELECT * FROM invalid_table WHERE s = ?").setString("s");

    try {
      ds.query(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
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
  public void simpleExecuteInsertFailure() throws CheckedFutureException {
    try {
      ds.execute("INSERT INTO invalid_table VALUES ('u')").get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void simpleExecuteUpdate() throws CheckedFutureException {
    ds.execute("UPDATE " + table + " SET s = 'u'").get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteUpdateFailure() throws CheckedFutureException {
    try {
      ds.execute("UPDATE invalid_table SET s = 'u'").get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void simpleExecuteDelete() throws CheckedFutureException {
    ds.execute("DELETE FROM " + table).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteDeleteFailure() throws CheckedFutureException {
    try {
      ds.execute("DELETE FROM invalid_table").get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void extendedExecuteInsertNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("INSERT INTO " + table + " VALUES ('u')");

    assertEquals(ds.execute(ps).get(timeout).longValue(), 1L);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteInsertNoParamFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("INSERT INTO invalid_table VALUES ('u')");

    try {
      ds.execute(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void extendedExecuteUpdateNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("UPDATE " + table + " SET s = 'u'");

    assertEquals(ds.execute(ps).get(timeout).longValue(), 1L);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteUpdateNoParamFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("UPDATE invalid_table SET s = 'u'");

    try {
      ds.execute(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void extendedExecuteDeleteNoParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("DELETE FROM " + table + " WHERE s = 's'");

    assertEquals(ds.execute(ps).get(timeout).longValue(), 1L);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteDeleteNoParamFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("DELETE FROM invalid_table WHERE s = 's'");

    try {
      ds.execute(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void extendedExecuteInsertWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("INSERT INTO " + table + " VALUES (?)").setString("u");

    assertEquals(ds.execute(ps).get(timeout).longValue(), 1L);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteInsertWithParamFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("INSERT INTO invalid_table VALUES (?)").setString("u");

    try {
      ds.execute(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void extendedExecuteUpdateWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("UPDATE " + table + " SET s = ?").setString("u");

    assertEquals(ds.execute(ps).get(timeout).longValue(), 1L);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteUpdteWithParamFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("UPDATE invalid_table SET s = ?").setString("u");

    try {
      ds.execute(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void extendedExecuteDeleteWithParam() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("DELETE FROM " + table + " WHERE s = ?").setString("s");

    assertEquals(ds.execute(ps).get(timeout).longValue(), 1L);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteDeleteWithParamFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("DELETE FROM invalid_table WHERE s = ?").setString("s");

    try {
      ds.execute(ps).get(timeout);
      assertTrue(false);
    } catch (final NdbcException ex) {
      assertTrue(ex.getMessage().contains("invalid_table"));
    }
  }

  @Test
  public void transactionSuccess() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("DELETE FROM " + table + " WHERE s = ?").setString("s");

    final long affectedRows = ds.transactional(() -> ds.execute(ps)).get(timeout);

    assertEquals(affectedRows, 1L);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void transactionLocalFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("DELETE FROM " + table + " WHERE s = ?").setString("s");

    try {
      ds.transactional(() -> ds.execute(ps).map(v -> {
        throw new IllegalStateException();
      })).get(timeout);
      assertTrue(false);
    } catch (final IllegalStateException ex) {
    }

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertTrue(rows.hasNext());
  }

  @Test
  public void transactionDBFailure() throws CheckedFutureException {
    final PreparedStatement ps = PreparedStatement.create("DELETE FROM INVALID_TABLE WHERE s = ?").setString("s");

    ds.transactional(() -> ds.execute(ps)).join(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertTrue(rows.hasNext());
  }

  protected abstract String streamQuery();

  @Test
  public void stream() throws CheckedFutureException {
    Flow<Row> f = ds.stream(PreparedStatement.create(streamQuery()));

    Promise<Void> result = Promise.apply();

    f.subscribe(new Subscriber<Row>() {

      private Subscription s        = null;
      private int          pending  = 0;
      private int          received = 0;

      @Override
      public void onSubscribe(Subscription s) {
        this.s = s;
        nextBatch();
      }

      private void nextBatch() {
        pending = 10;
        s.request(10);
      }

      @Override
      public void onNext(Row t) {
        received++;
        pending--;
        if (pending < 0)
          result.setException(new AssertionError("unexpected value"));
        else if (pending == 0) {
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            throw new RuntimeException(e);
          }
          nextBatch();
        }
      }

      @Override
      public void onError(Throwable t) {
        result.setException(t);
      }

      @Override
      public void onComplete() {
        if (received != 1000)
          result.setException(new AssertionError("received != 100"));
        else
          result.setValue(null);
      }
    });

    result.get(timeout);
  }

}
