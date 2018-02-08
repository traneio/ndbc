package io.trane.ndbc.mysql.netty4;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.Row;
import org.junit.After;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class DataSourceTest extends TestEnv {

  @Test
  public void simpleQuery() throws CheckedFutureException {
    // use to test locally docker run -e MYSQL_ROOT_PASSWORD=mysql -p 3306:3306 -d mysql
    final Iterator<Row> rows = ds.query("SELECT 'TEST' as testRow").get(timeout).iterator();
    Row row = rows.next();
    assertEquals(row.column(0).getString(), "TEST");
    assertEquals(row.columnNames().get(0), "testRow");
    assertFalse(rows.hasNext());
  }

  @After
  public void close() throws CheckedFutureException {
    ds.close().get(timeout);
  }
}
