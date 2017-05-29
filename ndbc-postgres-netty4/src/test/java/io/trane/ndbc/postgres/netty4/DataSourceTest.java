package io.trane.ndbc.postgres.netty4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public class DataSourceTest {

  private Config config = new Config("io.trane.ndbc.postgres.netty4.DataSourceSupplier",
      Charset.forName("UTF-8"), "postgres",
      Optional.of("postgres"), Optional.of("postgres"),
      "localhost", 5432, 10, 10, Duration.ofDays(1), new HashMap<>());

  private DataSource ds = DataSource.fromConfig(config);

  private Duration timeout = Duration.ofSeconds(1);

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
    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();

    assertEquals(rows.next().getString(0), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedQueryNoParams() throws CheckedFutureException {
    PreparedStatement ps = PreparedStatement.apply("SELECT * FROM test");

    Iterator<Row> rows = ds.query(ps).get(timeout).iterator();
    assertEquals(rows.next().getString(0), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedQueryWithParams() throws CheckedFutureException {
    PreparedStatement ps = PreparedStatement.apply("SELECT * FROM test WHERE s = ?")
        .bind("s");

    Iterator<Row> rows = ds.query(ps).get(timeout).iterator();
    assertEquals(rows.next().getString(0), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteInsert() throws CheckedFutureException {
    ds.execute("INSERT INTO test VALUES ('u')");

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().getString(0), "s");
    assertEquals(rows.next().getString(0), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteUpdate() throws CheckedFutureException {
    ds.execute("UPDATE test SET s = 'u'");

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().getString(0), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteDelete() throws CheckedFutureException {
    ds.execute("DELETE FROM test");

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteInsertNoParam() throws CheckedFutureException {
    PreparedStatement ps = PreparedStatement.apply("INSERT INTO test VALUES ('u')");

    ds.execute(ps).get(timeout);

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().getString(0), "s");
    assertEquals(rows.next().getString(0), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteUpdateNoParam() throws CheckedFutureException {
    PreparedStatement ps = PreparedStatement.apply("UPDATE test SET s = 'u'");

    ds.execute(ps).get(timeout);

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().getString(0), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteDeleteNoParam() throws CheckedFutureException {
    PreparedStatement ps = PreparedStatement.apply("DELETE FROM test WHERE s = 's'");

    ds.execute(ps).get(timeout);

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteInsertWithParam() throws CheckedFutureException {
    PreparedStatement ps = PreparedStatement.apply("INSERT INTO test VALUES (?)").bind("u");

    ds.execute(ps).get(timeout);

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().getString(0), "s");
    assertEquals(rows.next().getString(0), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteUpdateWithParam() throws CheckedFutureException {
    PreparedStatement ps = PreparedStatement.apply("UPDATE test SET s = ?").bind("u");

    ds.execute(ps).get(timeout);

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertEquals(rows.next().getString(0), "u");
    assertFalse(rows.hasNext());
  }

  @Test
  public void extendedExecuteDeleteWithParam() throws CheckedFutureException {
    PreparedStatement ps = PreparedStatement.apply("DELETE FROM test WHERE s = ?").bind("s");

    ds.execute(ps).get(timeout);

    Iterator<Row> rows = ds.query("SELECT * FROM test").get(timeout).iterator();
    assertFalse(rows.hasNext());
  }
}
