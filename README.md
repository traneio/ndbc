
# Non-blocking Database Connectivity (NDBC)

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
import io.trane.future.Future;
import io.trane.ndbc.*;
import io.trane.ndbc.postgres.*;
import java.time.Duration;
import java.util.List;

// Create a Config with an Embedded Postgres
Config config = Config.create("io.trane.ndbc.postgres.netty4.DataSourceSupplier", "localhost", 0, "user")
                      .database("test_schema")
                      .password("test")
                      .embedded("io.trane.ndbc.postgres.embedded.EmbeddedSupplier");

// Create a DataSource
DataSource<PreparedStatement, Row> ds = DataSource.fromConfig(config);

// Define a timeout
Duration timeout = Duration.ofSeconds(10);

// Send a query to the db defining a timeout and receiving back a List
List<Row> rows = ds.query("SELECT 1 AS value").get(timeout);

// iterate over awesome strongly typed rows
rows.forEach(row -> System.out.println(row.getLong("value")));
```

## Creating a DataSource

### From Properties
```java
Properties p = new Properties();
p.setProperty("db.dataSourceSupplierClass", "io.trane.ndbc.postgres.netty4.DataSourceSupplier");
p.setProperty("db.host", "localhost");
p.setProperty("db.port", Integer.toString(5432));
p.setProperty("db.user", "user");
p.setProperty("db.password", "5tr0ngP@ssW0rd");
p.setProperty("db.database", "schema");

DataSource<PreparedStatement, Row> ds = DataSource.fromProperties("db", p);
```

### From Properties file

Using the `Properties` from previous example:

```java
import java.io.File;
import java.io.FileOutputStream;

File file = File.createTempFile("config", "fromPropertiesFile");
p.store(new FileOutputStream(file), "");

DataSource<PreparedStatement, Row> ds = DataSource.fromPropertiesFile("db", file.getAbsolutePath());
```

### From System Properties

Similar to the first example, but getting the system properties with `getProperties`:

```java
Properties p = System.getProperties();
p.setProperty("db.dataSourceSupplierClass", "io.trane.ndbc.postgres.netty4.DataSourceSupplier");
p.setProperty("db.host", "localhost");
p.setProperty("db.port", Integer.toString(0));
p.setProperty("db.user", "user");
p.setProperty("db.password", "5tr0ngP@ssW0rd");
p.setProperty("db.database", "schema");

DataSource<PreparedStatement, Row> ds = DataSource.fromSystemProperties("db");
```

### From Jdbc Url

```java
DataSource<PreparedStatement, Row> ds = DataSource.fromJdbcUrl("jdbc:postgresql://user:5tr0ngP@ssW0rd@localhost:5432/schema");
```

## Available configurations

| Property | Expected value |
| --- | --- |
| `dataSourceSupplierClass` (required) | `io.trane.ndbc.postgres.netty4.DataSourceSupplier` or `io.trane.ndbc.mysql.netty4.DataSourceSupplier` |
| `host` (required) | database host |
| `port` (required) | database port |
| `user` (required) | user accessing the database |
| `password` | password of the user accessing the database |
| `database` | schema name |
| `charset` | character encoding. If not informed, the default charset of the JVM will be used |
| `poolMaxSize` | maximum number of connections in the pool |
| `poolMaxWaiters` | maximum number of waiters for a connection |
| `poolValidationIntervalSeconds` | frequency to test connections in the pool |
| `connectionTimeoutSeconds` | maximum time that a connection can remain idle. After that, the pool can close the connection |
| `queryTimeoutSeconds` | maximum time to execute a query |
| `encodingClasses` | |
| `nioThreads` | |
| `embedded` | |
| `ssl` | |

## Querying

Let's use the pre-populated table `table_1` as an example:

| id | description |
| --- | --- |
| 1 | The Amazing Spiderman |
| 2 | Batman the Dark Knight |

```java
DataSource<PreparedStatement, Row> ds = DataSource.fromConfig(config);

ds.execute("CREATE TABLE table_1 (id integer, description varchar)").get(timeout);
ds.execute("INSERT INTO table_1 VALUES (1, 'The Amazing Spiderman')").get(timeout);
ds.execute("INSERT INTO table_1 VALUES (2, 'Batman the Dark Knight')").get(timeout);
```

### Simple Query

```java
Future<List<Row>> rows = ds.query("SELECT * from table_1");
```

### PreparedStatement

#### Without parameters

```java
PreparedStatement ps = PreparedStatement.create("SELECT * FROM table_1");

Future<List<Row>> rows = ds.query(ps);
```

#### With parameters

```java
PreparedStatement ps = PreparedStatement.create("SELECT * FROM table_1 WHERE id = ?").setInteger(1);

Future<List<Row>> rows = ds.query(ps);
```

## Actions - Insert, Update and Delete

The code to execute actions is similar to the one for queries, the only difference is that instead of using `query`, we will use `execute`, which always returns a `Future<Long>`, with the number of affected rows.

### Simple Execute

```java
Future<Long> futureInsertedRows = ds.execute("INSERT INTO table_1 VALUES (10, 'Avengers Assemble!')")

Future<Long> futureUpdatedRows = ds.execute("UPDATE table_1 SET description = 'Go Go Power Rangers'")

Future<Long> futureDeletedRows = ds.execute("DELETE FROM table_1 WHERE id = 10")
```

### PreparedStatement

#### Without parameters

```java
PreparedStatement ps = PreparedStatement.create("DELETE FROM table_1");

Future<Long> affectedRows = ds.execute(ps);
```

#### With parameters

```java
PreparedStatement ps = PreparedStatement.create("DELETE FROM table_1 WHERE id = ?").setInteger(2);

Future<Long> affectedRows = ds.execute(ps);
```

### Transactions

```java
PreparedStatement ps = PreparedStatement.create("DELETE FROM table_1 WHERE id = ?").setInteger(2);

Future<Long> affectedRows = ds.transactional(() -> ds.execute(ps));
```

## Code of Conduct

Please note that this project is released with a Contributor Code of Conduct. By participating in this project you agree to abide by its terms. See [CODE_OF_CONDUCT.md](https://github.com/traneio/ndbc/blob/master/CODE_OF_CONDUCT.md) for details.

## License

See the [LICENSE](https://github.com/traneio/ndbc/blob/master/LICENSE.txt) file for details.
