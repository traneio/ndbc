package test;

import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Optional;

import io.trane.ndbc.Config;
import io.trane.ndbc.postgres.netty4.DataSourceSupplier;

public class Test {

  public static void main(String[] args) {
    Config config = new Config(Charset.defaultCharset(), "postgres", Optional.of("postgres"), Optional.of("postgres"),
        "localhost", 5432, 10, 10, Duration.ofMinutes(1));
    DataSourceSupplier sup = new DataSourceSupplier(config);
    sup.get().execute("");
  }
}
