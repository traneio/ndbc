package io.trane.ndbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Test;

import io.trane.ndbc.Config.SSL;
import io.trane.ndbc.Config.SSL.Mode;

public class ConfigTest {

  private final String  dataSourceSupplierClass       = "some.Class";
  private final String  host                          = "somehost";
  private final int     port                          = 999;
  private final String  user                          = "some_user";
  private final Charset charset                       = Charset.availableCharsets().values().stream()
      .filter(e -> e != Charset.defaultCharset()).collect(Collectors.toSet()).iterator().next();
  private final String  password                      = "password";
  private final String  database                      = "database";
  private final int     poolMaxSize                   = 100;
  private final int     poolMaxWaiters                = 1000;
  private final int     poolValidationIntervalSeconds = 1;

  @Test
  public void create() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertEquals(c.dataSourceSupplierClass(), dataSourceSupplierClass);
    assertEquals(c.host(), host);
    assertEquals(c.port(), port);
    assertEquals(c.user(), user);
    assertEquals(c.charset(), Charset.defaultCharset());
    assertFalse(c.password().isPresent());
    assertFalse(c.database().isPresent());
    assertFalse(c.poolMaxSize().isPresent());
    assertFalse(c.poolMaxWaiters().isPresent());
    assertFalse(c.poolValidationInterval().isPresent());
    assertFalse(c.encodingClasses().isPresent());
    assertFalse(c.nioThreads().isPresent());
  }

  @Test
  public void charset() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertEquals(charset, c.charset(charset).charset());
  }

  @Test
  public void password() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final String password = "password";
    assertEquals(Optional.of(password), c.password(password).password());
  }

  @Test
  public void passwordOptionalEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertFalse(c.password(Optional.empty()).password().isPresent());
  }

  @Test
  public void passwordOptionalPresent() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final String password = "password";
    assertEquals(Optional.of(password), c.password(Optional.of(password)).password());
  }

  @Test
  public void database() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final String database = "database";
    assertEquals(Optional.of(database), c.database(database).database());
  }

  @Test
  public void databaseOptionalEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertFalse(c.database(Optional.empty()).database().isPresent());
  }

  @Test
  public void databaseOptionalPresent() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final String database = "database";
    assertEquals(Optional.of(database), c.database(Optional.of(database)).database());
  }

  @Test
  public void poolMaxSize() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final int poolMaxSize = 100;
    assertEquals(Optional.of(poolMaxSize), c.poolMaxSize(poolMaxSize).poolMaxSize());
  }

  @Test
  public void poolMaxSizeOptionalEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertFalse(c.poolMaxSize(Optional.empty()).poolMaxSize().isPresent());
  }

  @Test
  public void poolMaxSizeOptionalPresent() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final int poolMaxSize = 100;
    assertEquals(Optional.of(poolMaxSize), c.poolMaxSize(Optional.of(poolMaxSize)).poolMaxSize());
  }

  @Test
  public void poolMaxWaiters() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final int poolMaxWaiters = 100;
    assertEquals(Optional.of(poolMaxWaiters), c.poolMaxWaiters(poolMaxWaiters).poolMaxWaiters());
  }

  @Test
  public void poolMaxWaitersOptionalEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertFalse(c.poolMaxWaiters(Optional.empty()).poolMaxWaiters().isPresent());
  }

  @Test
  public void poolMaxWaitersOptionalPresent() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final int poolMaxWaiters = 100;
    assertEquals(Optional.of(poolMaxWaiters), c.poolMaxWaiters(Optional.of(poolMaxWaiters)).poolMaxWaiters());
  }

  @Test
  public void connectionTimeout() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final Duration connectionTimeout = Duration.ofSeconds(100);
    assertEquals(Optional.of(connectionTimeout),
        c.connectionTimeout(connectionTimeout).connectionTimeout());
  }

  @Test
  public void queryTimeout() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final Duration queryTimeout = Duration.ofSeconds(100);
    assertEquals(Optional.of(queryTimeout),
        c.queryTimeout(queryTimeout).queryTimeout());
  }

  @Test
  public void poolValidationInterval() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final Duration poolValidationInterval = Duration.ofSeconds(100);
    assertEquals(Optional.of(poolValidationInterval),
        c.poolValidationInterval(poolValidationInterval).poolValidationInterval());
  }

  @Test
  public void poolValidationIntervalOptionalEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertFalse(c.poolValidationInterval(Optional.empty()).poolValidationInterval().isPresent());
  }

  @Test
  public void poolValidationIntervalOptionalPresent() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final Duration poolValidationInterval = Duration.ofSeconds(100);
    assertEquals(Optional.of(poolValidationInterval),
        c.poolValidationInterval(Optional.of(poolValidationInterval)).poolValidationInterval());
  }

  @Test
  public void encodingClasses() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final Set<String> encodingClasses = new HashSet<>();
    encodingClasses.add("some.Class");
    assertEquals(Optional.of(encodingClasses), c.encodingClasses(encodingClasses).encodingClasses());
  }

  @Test
  public void encodingClassesOptionalEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertFalse(c.encodingClasses(Optional.empty()).encodingClasses().isPresent());
  }

  @Test
  public void encodingClassesOptionalPresent() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final Set<String> encodingClasses = new HashSet<>();
    encodingClasses.add("some.Class");
    assertEquals(Optional.of(encodingClasses), c.encodingClasses(Optional.of(encodingClasses)).encodingClasses());
  }

  @Test
  public void addEncodingClassEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final String encodingClass = "some.Class";
    final Optional<Set<String>> encodingClasses = c.addEncodingClass(encodingClass).encodingClasses();
    assertTrue(encodingClasses.isPresent());
    assertEquals(1, encodingClasses.get().size());
    assertEquals(encodingClass, encodingClasses.get().iterator().next());
  }

  @Test
  public void addEncodingClassEmptySet() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user).encodingClasses(new HashSet<>());
    final String encodingClass = "some.Class";
    final Optional<Set<String>> encodingClasses = c.addEncodingClass(encodingClass).encodingClasses();
    assertTrue(encodingClasses.isPresent());
    assertEquals(1, encodingClasses.get().size());
    assertEquals(encodingClass, encodingClasses.get().iterator().next());
  }

  @Test
  public void addEncodingClassNonEmptySet() {
    final String previousEncodingClass = "previous.Class";
    final Set<String> previous = new HashSet<>();
    previous.add(previousEncodingClass);
    final Config c = Config.create(dataSourceSupplierClass, host, port, user).encodingClasses(previous);
    final String encodingClass = "some.Class";
    final Optional<Set<String>> encodingClasses = c.addEncodingClass(encodingClass).encodingClasses();
    assertTrue(encodingClasses.isPresent());
    assertEquals(2, encodingClasses.get().size());
    previous.add(encodingClass);
    assertEquals(previous, encodingClasses.get());
  }

  @Test
  public void nioThreads() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final int nioThreads = 100;
    assertEquals(Optional.of(nioThreads), c.nioThreads(nioThreads).nioThreads());
  }

  @Test
  public void nioThreadsOptionalEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertFalse(c.nioThreads(Optional.empty()).nioThreads().isPresent());
  }

  @Test
  public void nioThreadsOptionalPresent() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final int nioThreads = 100;
    assertEquals(Optional.of(nioThreads), c.nioThreads(Optional.of(nioThreads)).nioThreads());
  }

  @Test
  public void ssl() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final SSL ssl = Config.SSL.create(Mode.REQUIRE);
    assertEquals(Optional.of(ssl), c.ssl(ssl).ssl());
  }

  @Test
  public void sslMode() {
    Mode mode = Mode.REQUIRE;
    final SSL ssl = Config.SSL.create(mode);
    assertEquals(ssl.mode(), mode);
  }

  @Test
  public void sslRootCert() {
    File file = new File("test");
    final SSL ssl = Config.SSL.create(Mode.REQUIRE).rootCert(file);
    assertEquals(ssl.rootCert(), Optional.of(file));
  }

  @Test
  public void sslOptionalEmpty() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    assertFalse(c.ssl(Optional.empty()).ssl().isPresent());
  }

  @Test
  public void sslOptionalPresent() {
    final Config c = Config.create(dataSourceSupplierClass, host, port, user);
    final SSL ssl = Config.SSL.create(Mode.VERIFY_CA);
    assertEquals(Optional.of(ssl), c.ssl(Optional.of(ssl)).ssl());
  }

  @Test
  public void fromProperties() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.dataSourceSupplierClass(), dataSourceSupplierClass);
    assertEquals(c.host(), host);
    assertEquals(c.port(), port);
    assertEquals(c.user(), user);
    assertEquals(c.charset(), Charset.defaultCharset());
    assertFalse(c.password().isPresent());
    assertFalse(c.database().isPresent());
    assertFalse(c.poolMaxSize().isPresent());
    assertFalse(c.poolMaxWaiters().isPresent());
    assertFalse(c.poolValidationInterval().isPresent());
    assertFalse(c.encodingClasses().isPresent());
    assertFalse(c.nioThreads().isPresent());
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesMissingDataSourceSupplierClass() {
    final Properties p = new Properties();
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    Config.fromProperties("db", p);
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesMissingHost() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    Config.fromProperties("db", p);
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesMissingPort() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.user", user);
    Config.fromProperties("db", p);
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesIinvalidValue() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.user", user);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.poolMaxWaiters", "not an int");
    Config.fromProperties("db", p);
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesMissingUser() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    Config.fromProperties("db", p);
  }

  @Test
  public void fromPropertiesCharset() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.charset", charset.toString());
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.charset(), charset);
  }

  @Test
  public void fromPropertiesPassword() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.password", password);
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.password(), Optional.of(password));
  }

  @Test
  public void fromPropertiesDatabase() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.database", database);
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.database(), Optional.of(database));
  }

  @Test
  public void fromPropertiesPoolMaxSize() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.poolMaxSize", Integer.toString(poolMaxSize));
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.poolMaxSize(), Optional.of(poolMaxSize));
  }

  @Test
  public void fromPropertiesPoolMaxWaiters() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.poolMaxWaiters", Integer.toString(poolMaxWaiters));
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.poolMaxWaiters(), Optional.of(poolMaxWaiters));
  }

  @Test
  public void fromPropertiesPoolValidationIntervalSeconds() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.poolValidationIntervalSeconds", Integer.toString(poolValidationIntervalSeconds));
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.poolValidationInterval(), Optional.of(Duration.ofSeconds(poolValidationIntervalSeconds)));
  }

  @Test
  public void fromPropertiesEncodingClassesEmpty() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.encodingClasses", "");
    final Config c = Config.fromProperties("db", p);
    assertTrue(c.encodingClasses().isPresent());
    assertTrue(c.encodingClasses().get().isEmpty());
  }

  @Test
  public void fromPropertiesEncodingClassesNinEmpty() {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.encodingClasses", "a,b");
    final Config c = Config.fromProperties("db", p);
    assertTrue(c.encodingClasses().isPresent());
    final Iterator<String> it = c.encodingClasses().get().iterator();
    assertEquals(it.next(), "a");
    assertEquals(it.next(), "b");
    assertFalse(it.hasNext());
  }

  @Test
  public void fromPropertiesSSLMode() {
    final Mode mode = Mode.PREFER;
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.ssl.mode", mode.toString());
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.ssl(), Optional.of(SSL.create(mode)));
  }

  @Test
  public void fromPropertiesSSLRootCert() {
    final Mode mode = Mode.PREFER;
    final String rootCert = "rootCert";
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.ssl.mode", mode.toString());
    p.setProperty("db.ssl.rootCert", rootCert);
    final Config c = Config.fromProperties("db", p);
    assertEquals(c.ssl(), Optional.of(SSL.create(mode, new File(rootCert))));
  }

  @Test
  public void fromPropertiesFile() throws FileNotFoundException, IOException {
    final Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    final File file = File.createTempFile("test", "fromPropertiesFile");
    p.store(new FileOutputStream(file), "");

    final Config c = Config.fromPropertiesFile("db", file.getAbsolutePath());
    assertEquals(c.dataSourceSupplierClass(), dataSourceSupplierClass);
    assertEquals(c.host(), host);
    assertEquals(c.port(), port);
    assertEquals(c.user(), user);
    assertEquals(c.charset(), Charset.defaultCharset());
    assertFalse(c.password().isPresent());
    assertFalse(c.database().isPresent());
    assertFalse(c.poolMaxSize().isPresent());
    assertFalse(c.poolMaxWaiters().isPresent());
    assertFalse(c.poolValidationInterval().isPresent());
    assertFalse(c.encodingClasses().isPresent());
    assertFalse(c.nioThreads().isPresent());
  }

  @Test
  public void fromSystemProperties() {
    final Properties p = System.getProperties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);

    final Config c = Config.fromSystemProperties("db");
    assertEquals(c.dataSourceSupplierClass(), dataSourceSupplierClass);
    assertEquals(c.host(), host);
    assertEquals(c.port(), port);
    assertEquals(c.user(), user);
    assertEquals(c.charset(), Charset.defaultCharset());
    assertFalse(c.password().isPresent());
    assertFalse(c.database().isPresent());
    assertFalse(c.poolMaxSize().isPresent());
    assertFalse(c.poolMaxWaiters().isPresent());
    assertFalse(c.poolValidationInterval().isPresent());
    assertFalse(c.encodingClasses().isPresent());
    assertFalse(c.nioThreads().isPresent());
  }
}
