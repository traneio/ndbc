package org.springframework.jdbc.datasource.embedded;

import static java.util.Collections.emptyList;
import static ru.yandex.qatools.embed.postgresql.EmbeddedPostgres.cachedRuntimeConfig;
import static ru.yandex.qatools.embed.postgresql.util.SocketUtil.findFreePort;

import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Driver;

import javax.sql.DataSource;

import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import ru.yandex.qatools.embed.postgresql.EmbeddedPostgres;
import ru.yandex.qatools.embed.postgresql.distribution.Version.Main;

public class PostgresEmbeddedDatabaseConfigurer extends AbstractEmbeddedDatabaseConfigurer {

  private final EmbeddedPostgres postgres;
  private final IRuntimeConfig cached;

  @Nullable
  private static PostgresEmbeddedDatabaseConfigurer instance;

  private final Class<? extends Driver> driverClass;

  private PostgresEmbeddedDatabaseConfigurer(final Class<? extends Driver> driverClass) {
    this.driverClass = driverClass;
    postgres = new EmbeddedPostgres(Main.V9_6);
    cached = cachedRuntimeConfig(Paths.get(System.getProperty("user.home"), ".springframework", "embedded_postgres"));
  }

  @SuppressWarnings("unchecked")
  public static synchronized EmbeddedDatabaseConfigurer getInstance() throws ClassNotFoundException {
    if (instance == null) {
      instance = new PostgresEmbeddedDatabaseConfigurer((Class<? extends Driver>) ClassUtils
          .forName("org.postgresql.Driver", PostgresEmbeddedDatabaseConfigurer.class.getClassLoader()));
    }
    return instance;
  }

  @Override
  public void configureConnectionProperties(final ConnectionProperties properties, final String databaseName) {
    String url;
    try {
      url = postgres.start(cached, "localhost", findFreePort(), databaseName, "sa", "sa", emptyList());
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }

    properties.setDriverClass(this.driverClass);
    properties.setUrl(url);
  }

  @Override
  public void shutdown(final DataSource dataSource, final String databaseName) {
    postgres.stop();
  }
}