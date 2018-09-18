package io.trane.ndbc.postgres.embedded;

import static java.util.Arrays.asList;
import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import io.trane.future.Future;
import io.trane.future.FuturePool;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.distribution.Version.Main;
import ru.yandex.qatools.embed.postgresql.util.SocketUtil;

public class Embedded {

  private static final Logger log = Logger.getLogger(Embedded.class.getName());

  private static final FuturePool pool = FuturePool.apply(Executors.newCachedThreadPool());

  private static final List<String> DEFAULT_ADD_PARAMS = asList(
      "-E", "SQL_ASCII",
      "--locale=C",
      "--lc-collate=C",
      "--lc-ctype=C");

  public static enum Version {
    V9_5, V9_6;

    private Main main;

    private Version() {
      this.main = Main.valueOf(toString());
    }
  }

  public static final Future<DataSource> dataSource(Version version, Config config) {
    return pool.async(() -> {
      log.info("Starting embedded postgres " + version + " on port " + config.port());

      final EmbeddedPostgres postgres = new EmbeddedPostgres(version.main);

      IRuntimeConfig cached = cachedRuntimeConfig(
          Paths.get(System.getProperty("user.home"), ".ndbc", "embedded_postgres"));

      String password = config.password().orElseGet(() -> {
        throw new UnsupportedOperationException("Embedded postgres requires a password");
      });

      try {
        postgres.start(cached, config.host(), config.port(), config.database().orElse(EmbeddedPostgres.DEFAULT_DB_NAME),
            config.user(), password, DEFAULT_ADD_PARAMS);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      Runtime.getRuntime().addShutdownHook(new Thread(() -> postgres.stop()));

      log.info("postgres " + version + " started");
      return DataSource.fromConfig(config);
    });
  }

  public static int findFreePort() {
    return SocketUtil.findFreePort();
  }
}
