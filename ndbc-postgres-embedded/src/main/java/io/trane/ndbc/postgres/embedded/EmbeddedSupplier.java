package io.trane.ndbc.postgres.embedded;

import static java.util.Arrays.asList;
import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.logging.Logger;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import io.trane.future.Future;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.distribution.Version;
import ru.yandex.qatools.embed.postgresql.util.SocketUtil;

public class EmbeddedSupplier implements Supplier<DataSource> {

  private static final Logger log = Logger.getLogger(EmbeddedSupplier.class.getName());

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
  public final DataSource get() {
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

    DataSource underlying = DataSource.fromConfig(config.embedded(Optional.empty()));

    return new DataSource() {

      @Override
      public <T> Future<T> transactional(Supplier<Future<T>> supplier) {
        return underlying.transactional(supplier);
      }

      @Override
      public Future<List<Row>> query(PreparedStatement query) {
        return underlying.query(query);
      }

      @Override
      public Future<List<Row>> query(String query) {
        return underlying.query(query);
      }

      @Override
      public Future<Long> execute(PreparedStatement statement) {
        return underlying.execute(statement);
      }

      @Override
      public Future<Long> execute(String statement) {
        return underlying.execute(statement);
      }

      @Override
      public Config config() {
        return config;
      }

      @Override
      public Future<Void> close() {
        return underlying.close().ensure(() -> postgres.stop());
      }
    };
  }
}
