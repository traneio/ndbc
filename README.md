
# Non-blocking Database Conectivity (NDBC)

[![Build Status](https://travis-ci.org/traneio/ndbc.svg?branch=master)](https://travis-ci.org/traneio/ndbc)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.trane/ndbc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.trane/ndbc)
[![Javadoc](https://img.shields.io/badge/api-javadoc-green.svg)](http://trane.io/apidocs/ndbc/current/)
[![Join the chat at https://gitter.im/traneio/ndbc](https://img.shields.io/badge/gitter-join%20chat-green.svg)](https://gitter.im/traneio/ndbc?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This project goal is to provide a full asyncronous approach to handle databases.

## What is the problem with JDBC?

JDBC is a synchronous API, meaning that it blocks the Thread in use by any class using it.

## Getting started

The library binaries are distributed through maven central. Click on the maven central badge for information on how to add the library dependency to your project:

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.trane/ndbc/badge.svg)](https://maven-badges.herokuapp.com/maven-central/io.trane/ndbc)

Please refer to the Javadoc for detailed information about the library and its features:

[![Javadoc](https://img.shields.io/badge/api-javadoc-green.svg)](http://trane.io/apidocs/ndbc/current/)

## 1 minute example

```java
import io.trane.ndbc.*;
import io.trane.ndbc.postgres.*;
import java.time.Duration;

// Create a Config
Config config = Config.create("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 0, "user")
                      .database("test_schema")
                      .password("test")
                      .embedded("io.trane.ndbc.postgres.embedded.EmbeddedSupplier");

// Create a DataSource
PostgresDataSource ds = PostgresDataSource.fromConfig(config);

// Define a timeout
Duration timeout = Duration.ofSeconds(10);

// Send a query to the db defining a timeout and receiving back an iterator
Iterator<PostgresRow> rows = ds.query("SELECT 1 AS value").get(timeout).iterator();

// iterate over awesome strongly typed rows
rows.forEachRemaining(row -> System.out.println(row.getLong("value")));
```

## Creating a DataSource

### From Properties
```java
Properties p = new Properties();
p.setProperty("db.dataSourceSupplierClass", "io.trane.ndbc.postgres.netty4.DataSourceSupplier");
p.setProperty("db.host", "localhost");
p.setProperty("db.port", Integer.toString(0));
p.setProperty("db.user", "user");
p.setProperty("db.password", "test");
p.setProperty("db.database", "test_schema");
p.setProperty("db.embedded.supplierClass", "io.trane.ndbc.postgres.embedded.EmbeddedSupplier");

PostgresDataSource ds = PostgresDataSource.fromProperties("db", p);
```

### From a Properties file

Using the `Properties` from the previous example:

```java
File file = File.createTempFile("config", "fromPropertiesFile");
p.store(new FileOutputStream(file), "");

PostgresDataSource ds = PostgresDataSource.fromPropertiesFile("db", file.getAbsolutePath());
```

### From System Properties

Similar to the first example, but getting the system properties with `getProperties`:

```java
Properties p = System.getProperties();
// p.setProperty(...);

PostgresDataSource ds = PostgresDataSource.fromSystemProperties("db");
```

### From Jdbc Url

```java
PostgresDataSource ds = PostgresDataSource.fromJdbcUrl("jdbc:postgresql://user:test@localhost:0/test_schema");
```

## Code of Conduct

Please note that this project is released with a Contributor Code of Conduct. By participating in this project you agree to abide by its terms. See [CODE_OF_CONDUCT.md](https://github.com/traneio/ndbc/blob/master/CODE_OF_CONDUCT.md) for details.

## License

See the [LICENSE](https://github.com/traneio/ndbc/blob/master/LICENSE.txt) file for details.
