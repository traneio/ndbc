package io.trane.ndbc.mysql.embedded;

import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Paths;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import com.wix.mysql.EmbeddedMysql;
import com.wix.mysql.EmbeddedMysql.Builder;
import com.wix.mysql.config.DownloadConfig;
import com.wix.mysql.config.MysqldConfig;
import com.wix.mysql.config.SchemaConfig;

import io.trane.future.Future;
import io.trane.future.FuturePool;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;

public class Embedded {

  private static final Logger log = Logger.getLogger(Embedded.class.getName());

  private static final FuturePool pool = FuturePool.apply(Executors.newCachedThreadPool());

  public static enum Version {
    V5_5, V5_6, V5_7;

    private com.wix.mysql.distribution.Version latest;

    private Version() {
      this.latest = com.wix.mysql.distribution.Version.valueOf(toString().toLowerCase() + "_latest");
    }
  }

  public static final Future<DataSource> dataSource(Version version, Config config) {
    return pool.async(() -> {
      log.info("Starting embedded mysql " + version + " on port " + config.port());

      String password = config.password().orElseGet(() -> {
        throw new UnsupportedOperationException("Embedded mysql requires a password");
      });

      DownloadConfig downloadConfig = DownloadConfig.aDownloadConfig()
          .withCacheDir(Paths.get(System.getProperty("user.home"), ".ndbc", "embedded_mysql").toString())
          .build();

      MysqldConfig mysqldConfig = MysqldConfig.aMysqldConfig(version.latest)
          .withPort(config.port())
          .withUser(config.user(), password)
          .build();

      Builder builder = EmbeddedMysql.anEmbeddedMysql(mysqldConfig, downloadConfig);

      config.database().ifPresent(db -> builder.addSchema(SchemaConfig.aSchemaConfig(db).build()));

      EmbeddedMysql mysql = builder.start();

      Runtime.getRuntime().addShutdownHook(new Thread(() -> mysql.stop()));

      log.info("mysql " + version + " started");
      return DataSource.fromConfig(config);
    });
  }

  public static int findFreePort() {
    try (ServerSocket socket = new ServerSocket(0)) {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
