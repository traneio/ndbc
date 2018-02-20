package io.trane.ndbc.postgres.netty4;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.Config;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.test.DataSourceTest;

public class PostgresDataSourceTest extends DataSourceTest {

	private static final Config config = Config
	    .apply("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 5432, "postgres").password("postgres")
	    .poolValidationInterval(Duration.ofSeconds(1)).poolMaxSize(1).poolMaxWaiters(0); // .ssl(SSL.apply(SSL.Mode.REQUIRE));

	public PostgresDataSourceTest() {
		super(config);
	}

	@Before
	public void recreateSchema() throws CheckedFutureException {
		ds.execute("DROP TABLE IF EXISTS " + table).get(timeout);
		ds.execute("CREATE TABLE " + table + " (s varchar)").get(timeout);
		ds.execute("INSERT INTO " + table + " VALUES ('s')").get(timeout);
	}

	@After
	public void close() throws CheckedFutureException {
		ds.close().get(timeout);
	}
}
