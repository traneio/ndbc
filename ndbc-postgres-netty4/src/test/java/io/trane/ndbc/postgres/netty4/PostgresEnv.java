package io.trane.ndbc.postgres.netty4;

import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig;

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
import ru.yandex.qatools.embed.postgresql.distribution.Version;

public class PostgresEnv {

  public static Collection<Object[]> dataSources() {
    return Arrays.asList(new Object[][] { ds(Version.Main.V9_6) });
  }

  private static final Object[] ds(IVersion version) {
    try {
      final EmbeddedPostgres postgres = new EmbeddedPostgres(version);
      postgres.start(cachedRuntimeConfig(Paths.get(System.getProperty("java.io.tmpdir"), "pgembed")));
      PostgresConfig cfg = postgres.getConfig().get();
      Config config = Config
          .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier",
              cfg.net().host(), cfg.net().port(),
              cfg.credentials().username())
          .password(cfg.credentials().password())
          .poolValidationInterval(Duration.ofSeconds(1))
          .poolMaxSize(1)
          .poolMaxWaiters(0)
          .connectionTimeout(Duration.ofSeconds(1))
          .queryTimeout(Duration.ofSeconds(1));
      return new Object[] { DataSource.fromConfig(config), version.toString() };
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static final Config instance;

  static {
    try {
      final EmbeddedPostgres postgres = new EmbeddedPostgres(Version.Main.V9_6);
      postgres.start(cachedRuntimeConfig(Paths.get(System.getProperty("java.io.tmpdir"), "pgembed")));
      PostgresConfig cfg = postgres.getConfig().get();
      instance = Config
          .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier",
              cfg.net().host(), cfg.net().port(),
              cfg.credentials().username())
          .password(cfg.credentials().password())
          .poolValidationInterval(Duration.ofSeconds(1))
          .poolMaxSize(1)
          .poolMaxWaiters(0)
          .connectionTimeout(Duration.ofSeconds(1))
          .queryTimeout(Duration.ofSeconds(1));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
