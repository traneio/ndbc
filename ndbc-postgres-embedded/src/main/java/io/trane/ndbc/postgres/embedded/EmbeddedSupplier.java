package io.trane.ndbc.postgres.embedded;

import static java.util.Arrays.asList;
import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.distribution.Version;
import ru.yandex.qatools.embed.postgresql.util.SocketUtil;

public class EmbeddedSupplier implements Supplier<DataSource<PreparedStatement, Row>> {

  private static final Logger log = LoggerFactory.getLogger(EmbeddedSupplier.class);

  private static final List<String> DEFAULT_ADD_PARAMS = asList(
      "-E", "SQL_ASCII",
      "--locale=C",
      "--lc-collate=C",
      "--lc-ctype=C");

  private final Config       config;
  private final Version.Main version;

  public EmbeddedSupplier(Config config, Optional<String> version) {
    if (config.port() == 0)
      this.config = config.port(SocketUtil.findFreePort());
    else
      this.config = config;
    this.version = version.map(Version.Main::valueOf).orElse(Version.Main.V9_6);
  }

  @Override
  public final DataSource<PreparedStatement, Row> get() {
    log.info("Starting embedded postgres " + version + " on port " + config.port());

    final EmbeddedPostgres postgres = new EmbeddedPostgres(version);

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

    return DataSource.fromConfig(config.embedded(Optional.empty()));
  }
}
