package io.trane.ndbc.postgres.netty4;

import java.nio.charset.Charset;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;
import io.trane.ndbc.postgres.Connection;
import io.trane.ndbc.postgres.netty4.DataSourceSupplier;

public class Test {

  public static void main(String[] args) throws SQLException, CheckedFutureException {
    // Test.jbdc();
    Test.trane();
  }

  private static void trane() throws CheckedFutureException {
    Properties properties = new Properties();
    properties.setProperty("ds.dataSourceSupplierClass", "io.trane.ndbc.postgres.netty4.DataSourceSupplier");
    properties.setProperty("ds.user", "postgres");
    properties.setProperty("ds.password", "postgres");
    properties.setProperty("ds.host", "localhost");
    DataSource ds = DataSource.fromProperties("ds", properties);
    PreparedStatement ps = PreparedStatement.create("select * from test");
    // .setString(0, "s");
    ResultSet i = ds.query(ps).get(Duration.ofDays(1));
    System.out.println(i.iterator().next().getString(0));

    // r = ds.query(ps).get(Duration.ofDays(1));
    // System.out.println(r.iterator().next().getString("s"));
  }

  // private static void jbdc() throws SQLException {
  // String url = "jdbc:postgresql://localhost/postgres";
  // Properties props = new Properties();
  // props.setProperty("user", "postgres");
  // props.setProperty("password", "postgres");
  // Connection conn = DriverManager.getConnection(url, props);
  // ResultSet s = conn.prepareStatement("select 1").executeQuery();
  // System.out.println(s.toString());
  // }
}
