package io.trane.ndbc.postgres.netty4;

import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_5;
import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_6;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Collection;

import de.flapdoodle.embed.process.distribution.IVersion;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

public class PostgresEnv {

  public static final Collection<Object[]> dataSources = Arrays.asList(new Object[][] { ds(V9_5), ds(V9_6) });

  private static final Object[] ds(IVersion version) {
    try {
      final EmbeddedPostgres postgres = new EmbeddedPostgres(version);
      postgres.start(cachedRuntimeConfig(Paths.get(System.getProperty("user.home"), ".ndbc")));
      PostgresConfig cfg = postgres.getConfig().get();
      Config config = Config
          .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier",
              cfg.net().host(), cfg.net().port(),
              cfg.credentials().username())
          .password(cfg.credentials().password())
          .poolValidationInterval(Duration.ofSeconds(1))
          .connectionTimeout(Duration.ofSeconds(1))
          .queryTimeout(Duration.ofSeconds(1));

      Runtime.getRuntime().addShutdownHook(new Thread(() -> postgres.stop()));

      return new Object[] { DataSource.fromConfig(config), "PG " + version };
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
