package io.trane.ndbc;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.function.Supplier;

import org.junit.Test;

import io.trane.future.Future;

public class DataSourceTest {

	private static String host = "host";
	private static int port = 1112;
	private static String user = "user";
	private static Config cfg = Config.apply(DSSupplier.class.getName(), host, port, user);

	@Test
	public void fromSystemProperties() {
		initProperties(System.getProperties());
		assertEquals(ds, DataSource.fromSystemProperties("db"));
	}

	@Test
	public void fromPropertiesFile() throws FileNotFoundException, IOException {
		final Properties p = new Properties();
		initProperties(p);
		final File file = File.createTempFile("test", "fromPropertiesFile");
		p.store(new FileOutputStream(file), "");
		assertEquals(ds, DataSource.fromPropertiesFile("db", file.getAbsolutePath()));
	}

	@Test
	public void fromProperties() {
		final Properties p = new Properties();
		initProperties(p);
		assertEquals(ds, DataSource.fromProperties("db", p));
	}

	@Test
	public void fromConfig() {
		assertEquals(ds, DataSource.fromConfig(cfg));
	}

	@Test(expected = RuntimeException.class)
	public void fromFail() {
		final Config cfg = Config.apply("not a class", host, port, user);
		DataSource.fromConfig(cfg);
	}

	private void initProperties(final Properties p) {
		p.setProperty("db.dataSourceSupplierClass", DSSupplier.class.getName());
		p.setProperty("db.host", host);
		p.setProperty("db.port", Integer.toString(port));
		p.setProperty("db.user", user);
	}

	static class DSSupplier implements Supplier<DataSource> {
		public DSSupplier(final Config c) {
		}

		@Override
		public DataSource get() {
			return ds;
		}
	}

	private static DataSource ds = new DataSource() {

		@Override
		public <T> Future<T> transactional(final Supplier<Future<T>> supplier) {
			return null;
		}

		@Override
		public Future<List<Row>> query(final PreparedStatement query) {
			return null;
		}

		@Override
		public Future<List<Row>> query(final String query) {
			return null;
		}

		@Override
		public Future<Long> execute(final PreparedStatement statement) {
			return null;
		}

		@Override
		public Future<Long> execute(final String statement) {
			return null;
		}

		@Override
		public Future<Void> close() {
			return null;
		}
	};
}
