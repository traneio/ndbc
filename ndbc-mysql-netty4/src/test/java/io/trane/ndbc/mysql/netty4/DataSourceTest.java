package io.trane.ndbc.mysql.netty4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

//use to test locally docker run -e MYSQL_ROOT_PASSWORD=mysql -p 3306:3306 -d mysql
public class DataSourceTest extends TestEnv {

  private static int tableSuffix = 1;
  private final String table = "table_" + tableSuffix++;

  @Before
  public void recreateSchema() throws CheckedFutureException {
    ds.execute("DROP TABLE IF EXISTS " + table).get(timeout);
    ds.execute("CREATE TABLE " + table + " (s varchar(20))").get(timeout);
    ds.execute("INSERT INTO " + table + " VALUES ('s')").get(timeout);
  }

  @After
  public void close() throws CheckedFutureException {
    ds.close().get(timeout);
  }

  @Test
  public void simpleQuery() throws CheckedFutureException {
    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();

    assertEquals(rows.next().column(0).getString(), "s");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteInsert() throws CheckedFutureException {
    ds.execute("INSERT INTO " + table + " VALUES ('j')").get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "j");
    assertFalse(rows.hasNext());
  }

  @Test
  public void simpleExecuteUpdate() throws CheckedFutureException {
    ds.execute("UPDATE " + table + " SET s = 'j'").get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "j");
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
    final PreparedStatement ps = PreparedStatement.apply("INSERT INTO " + table + " VALUES ('z')");

    ds.execute(ps).get(timeout);

    final Iterator<Row> rows = ds.query("SELECT * FROM " + table).get(timeout).iterator();
    assertEquals(rows.next().column(0).getString(), "s");
    assertEquals(rows.next().column(0).getString(), "z");
    assertFalse(rows.hasNext());
  }
}
