package io.trane.ndbc.mysql.embedded;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.EmbeddedMysql.Builder;
import com.wix.mysql.config.DownloadConfig;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.config.SchemaConfig;
import com.wix.mysql.distribution.Version;

import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.datasource.ProxyDataSource;

public class EmbeddedSupplier implements Supplier<DataSource<PreparedStatement, Row>> {

  private static final Logger log = LoggerFactory.getLogger(EmbeddedSupplier.class);

  private final Config  config;
  private final Version version;

  public EmbeddedSupplier(final Config config, final Optional<String> version) {
    if (config.port() == 0)
      this.config = config.port(findFreePort());
    else
      this.config = config;
    this.version = version.map(Version::valueOf).orElse(Version.v5_7_latest);
  }

  @Override
  public DataSource<PreparedStatement, Row> get() {
    log.info("Starting embedded mysql " + version + " on port " + config.port());

    final String password = config.password().orElseGet(() -> {
      throw new UnsupportedOperationException("Embedded mysql requires a password");
    });

    final DownloadConfig downloadConfig = DownloadConfig.aDownloadConfig()
        .withCacheDir(Paths.get(System.getProperty("user.home"), ".ndbc", "embedded_mysql").toString())
        .build();

    final MysqldConfig mysqldConfig = MysqldConfig.aMysqldConfig(version)
        .withPort(config.port())
        .withUser(config.user(), password)
        .build();

    final Builder builder = EmbeddedMysql.anEmbeddedMysql(mysqldConfig, downloadConfig);

    config.database().ifPresent(db -> builder.addSchema(SchemaConfig.aSchemaConfig(db).build()));

    final EmbeddedMysql mysql = builder.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> mysql.stop()));

    log.info("mysql " + version + " started");

    DataSource<PreparedStatement, Row> underlying = DataSource.fromConfig(config.embedded(Optional.empty()));
    return new ProxyDataSource<PreparedStatement, Row>(underlying) {
      @Override
      public Config config() {
        return EmbeddedSupplier.this.config;
      }
    };
  }

  private static int findFreePort() {
    try (ServerSocket socket = new ServerSocket(0)) {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    } catch (final IOException e) {
      throw new RuntimeException(e);
    }
  }
}
