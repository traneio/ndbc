package io.trane.ndbc.mysql.netty4;

import java.time.Duration;

import io.trane.ndbc.Config;
import io.trane.ndbc.test.DataSourceTest;

public class MysqlDataSourceTest extends DataSourceTest {

	private static final Config config = Config
			.apply("io.trane.ndbc.mysql.netty4.DataSourceSupplier", "localhost", 3306, "root").database("mysql")
			.poolValidationInterval(Duration.ofSeconds(1)).poolMaxSize(1).poolMaxWaiters(0);

	public MysqlDataSourceTest() {
		super(config, "varchar(20)", "SELECT SLEEP(999)");
	}
}
