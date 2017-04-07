package test;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.Optional;
import java.util.Properties;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.Config;
import io.trane.ndbc.postgres.netty4.DataSourceSupplier;

public class Test {

  public static void main(String[] args) throws SQLException, CheckedFutureException {
//    Test.jbdc();
    Test.trane();
  }

  private static void trane() throws CheckedFutureException {
    Config config = new Config(Charset.forName("UTF-8"), "postgres",
        Optional.of("postgres"), Optional.of("postgres"),
        "localhost", 5432, 10, 10, Duration.ofMinutes(1));
    DataSourceSupplier sup = new DataSourceSupplier(config);
    int r = sup.get().execute("update test set s = 's'").get(Duration.ofDays(1));
    System.out.println(r);
  }

  private static void jbdc() throws SQLException {
    String url = "jdbc:postgresql://localhost/postgres";
    Properties props = new Properties();
    props.setProperty("user", "postgres");
    props.setProperty("password", "postgres");
    Connection conn = DriverManager.getConnection(url, props);
    ResultSet s = conn.prepareStatement("select 1").executeQuery();
    System.out.println(s.toString());
  }
}
