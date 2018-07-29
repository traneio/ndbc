package io.trane.ndbc.mysql.netty4;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.Config;
import io.trane.ndbc.test.DataSourceTest;

public class MysqlDataSourceTest extends DataSourceTest {

	private static final Config config = Config
			.apply("io.trane.ndbc.mysql.netty4.DataSourceSupplier", "localhost", 3306, "root").database("mysql")
			.poolValidationInterval(Duration.ofSeconds(1)).poolMaxSize(1).poolMaxWaiters(0);

	public MysqlDataSourceTest() {
		super(config);
	}

	@Before
	public void recreateSchema() throws CheckedFutureException {
		ds.execute("DROP TABLE IF EXISTS " + table).get(timeout);
		ds.execute("CREATE TABLE " + table + " (s varchar(20))").get(timeout);
		ds.execute("INSERT INTO " + table + " VALUES ('s')").get(timeout);
	}

	@After
	public void close() throws CheckedFutureException {
		ds.close().get(timeout);
	}
}
