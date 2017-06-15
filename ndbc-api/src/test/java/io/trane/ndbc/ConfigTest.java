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

  private String  dataSourceSupplierClass       = "some.Class";
  private String  host                          = "somehost";
  private int     port                          = 999;
  private String  user                          = "some_user";
  private Charset charset                       = Charset.availableCharsets().values().stream()
      .filter(e -> e != Charset.defaultCharset()).collect(Collectors.toSet()).iterator().next();
  private String  password                      = "password";
  private String  database                      = "database";
  private int     poolMaxSize                   = 100;
  private int     poolMaxWaiters                = 1000;
  private int     poolValidationIntervalSeconds = 1;

  @Test
  public void apply() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
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
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertEquals(charset, c.charset(charset).charset());
  }

  @Test
  public void password() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    String password = "password";
    assertEquals(Optional.of(password), c.password(password).password());
  }

  @Test
  public void passwordOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.password(Optional.empty()).password().isPresent());
  }

  @Test
  public void passwordOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    String password = "password";
    assertEquals(Optional.of(password), c.password(Optional.of(password)).password());
  }

  @Test
  public void database() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    String database = "database";
    assertEquals(Optional.of(database), c.database(database).database());
  }

  @Test
  public void databaseOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.database(Optional.empty()).database().isPresent());
  }

  @Test
  public void databaseOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    String database = "database";
    assertEquals(Optional.of(database), c.database(Optional.of(database)).database());
  }

  @Test
  public void poolMaxSize() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    int poolMaxSize = 100;
    assertEquals(Optional.of(poolMaxSize), c.poolMaxSize(poolMaxSize).poolMaxSize());
  }

  @Test
  public void poolMaxSizeOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.poolMaxSize(Optional.empty()).poolMaxSize().isPresent());
  }

  @Test
  public void poolMaxSizeOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    int poolMaxSize = 100;
    assertEquals(Optional.of(poolMaxSize), c.poolMaxSize(Optional.of(poolMaxSize)).poolMaxSize());
  }

  @Test
  public void poolMaxWaiters() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    int poolMaxWaiters = 100;
    assertEquals(Optional.of(poolMaxWaiters), c.poolMaxWaiters(poolMaxWaiters).poolMaxWaiters());
  }

  @Test
  public void poolMaxWaitersOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.poolMaxWaiters(Optional.empty()).poolMaxWaiters().isPresent());
  }

  @Test
  public void poolMaxWaitersOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    int poolMaxWaiters = 100;
    assertEquals(Optional.of(poolMaxWaiters), c.poolMaxWaiters(Optional.of(poolMaxWaiters)).poolMaxWaiters());
  }

  @Test
  public void poolValidationInterval() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    Duration poolValidationInterval = Duration.ofSeconds(100);
    assertEquals(Optional.of(poolValidationInterval),
        c.poolValidationInterval(poolValidationInterval).poolValidationInterval());
  }

  @Test
  public void poolValidationIntervalOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.poolValidationInterval(Optional.empty()).poolValidationInterval().isPresent());
  }

  @Test
  public void poolValidationIntervalOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    Duration poolValidationInterval = Duration.ofSeconds(100);
    assertEquals(Optional.of(poolValidationInterval),
        c.poolValidationInterval(Optional.of(poolValidationInterval)).poolValidationInterval());
  }

  @Test
  public void encodingClasses() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    Set<String> encodingClasses = new HashSet<>();
    encodingClasses.add("some.Class");
    assertEquals(Optional.of(encodingClasses), c.encodingClasses(encodingClasses).encodingClasses());
  }

  @Test
  public void encodingClassesOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.encodingClasses(Optional.empty()).encodingClasses().isPresent());
  }

  @Test
  public void encodingClassesOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    Set<String> encodingClasses = new HashSet<>();
    encodingClasses.add("some.Class");
    assertEquals(Optional.of(encodingClasses), c.encodingClasses(Optional.of(encodingClasses)).encodingClasses());
  }

  @Test
  public void addEncodingClassEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    String encodingClass = "some.Class";
    Optional<Set<String>> encodingClasses = c.addEncodingClass(encodingClass).encodingClasses();
    assertTrue(encodingClasses.isPresent());
    assertEquals(1, encodingClasses.get().size());
    assertEquals(encodingClass, encodingClasses.get().iterator().next());
  }

  @Test
  public void addEncodingClassEmptySet() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user).encodingClasses(new HashSet<>());
    String encodingClass = "some.Class";
    Optional<Set<String>> encodingClasses = c.addEncodingClass(encodingClass).encodingClasses();
    assertTrue(encodingClasses.isPresent());
    assertEquals(1, encodingClasses.get().size());
    assertEquals(encodingClass, encodingClasses.get().iterator().next());
  }

  @Test
  public void addEncodingClassNonEmptySet() {
    String previousEncodingClass = "previous.Class";
    Set<String> previous = new HashSet<>();
    previous.add(previousEncodingClass);
    Config c = Config.apply(dataSourceSupplierClass, host, port, user).encodingClasses(previous);
    String encodingClass = "some.Class";
    Optional<Set<String>> encodingClasses = c.addEncodingClass(encodingClass).encodingClasses();
    assertTrue(encodingClasses.isPresent());
    assertEquals(2, encodingClasses.get().size());
    previous.add(encodingClass);
    assertEquals(previous, encodingClasses.get());
  }

  @Test
  public void nioThreads() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    int nioThreads = 100;
    assertEquals(Optional.of(nioThreads), c.nioThreads(nioThreads).nioThreads());
  }

  @Test
  public void nioThreadsOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.nioThreads(Optional.empty()).nioThreads().isPresent());
  }

  @Test
  public void nioThreadsOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    int nioThreads = 100;
    assertEquals(Optional.of(nioThreads), c.nioThreads(Optional.of(nioThreads)).nioThreads());
  }

  @Test
  public void ssl() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    SSL ssl = Config.SSL.apply(Mode.REQUIRE);
    assertEquals(Optional.of(ssl), c.ssl(ssl).ssl());
  }

  @Test
  public void sslOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.ssl(Optional.empty()).ssl().isPresent());
  }

  @Test
  public void sslOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    SSL ssl = Config.SSL.apply(Mode.VERIFY_CA);
    assertEquals(Optional.of(ssl), c.ssl(Optional.of(ssl)).ssl());
  }

  @Test
  public void fromProperties() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    Config c = Config.fromProperties("db", p);
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
    Properties p = new Properties();
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    Config.fromProperties("db", p);
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesMissingHost() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    Config.fromProperties("db", p);
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesMissingPort() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.user", user);
    Config.fromProperties("db", p);
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesIinvalidValue() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.user", user);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.poolMaxWaiters", "not an int");
    Config.fromProperties("db", p);
  }

  @Test(expected = RuntimeException.class)
  public void fromPropertiesMissingUser() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    Config.fromProperties("db", p);
  }

  @Test
  public void fromPropertiesCharset() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.charset", charset.toString());
    Config c = Config.fromProperties("db", p);
    assertEquals(c.charset(), charset);
  }

  @Test
  public void fromPropertiesPassword() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.password", password);
    Config c = Config.fromProperties("db", p);
    assertEquals(c.password(), Optional.of(password));
  }

  @Test
  public void fromPropertiesDatabase() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.database", database);
    Config c = Config.fromProperties("db", p);
    assertEquals(c.database(), Optional.of(database));
  }

  @Test
  public void fromPropertiesPoolMaxSize() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.poolMaxSize", Integer.toString(poolMaxSize));
    Config c = Config.fromProperties("db", p);
    assertEquals(c.poolMaxSize(), Optional.of(poolMaxSize));
  }

  @Test
  public void fromPropertiesPoolMaxWaiters() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.poolMaxWaiters", Integer.toString(poolMaxWaiters));
    Config c = Config.fromProperties("db", p);
    assertEquals(c.poolMaxWaiters(), Optional.of(poolMaxWaiters));
  }

  @Test
  public void fromPropertiesPoolValidationIntervalSeconds() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.poolValidationIntervalSeconds", Integer.toString(poolValidationIntervalSeconds));
    Config c = Config.fromProperties("db", p);
    assertEquals(c.poolValidationInterval(), Optional.of(Duration.ofSeconds(poolValidationIntervalSeconds)));
  }

  @Test
  public void fromPropertiesEncodingClassesEmpty() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.encodingClasses", "");
    Config c = Config.fromProperties("db", p);
    assertTrue(c.encodingClasses().isPresent());
    assertTrue(c.encodingClasses().get().isEmpty());
  }

  @Test
  public void fromPropertiesEncodingClassesNinEmpty() {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.encodingClasses", "a,b");
    Config c = Config.fromProperties("db", p);
    assertTrue(c.encodingClasses().isPresent());
    Iterator<String> it = c.encodingClasses().get().iterator();
    assertEquals(it.next(), "a");
    assertEquals(it.next(), "b");
    assertFalse(it.hasNext());
  }

  @Test
  public void fromPropertiesSSLMode() {
    Mode mode = Mode.PREFER;
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.ssl.mode", mode.toString());
    Config c = Config.fromProperties("db", p);
    assertEquals(c.ssl(), Optional.of(SSL.apply(mode)));
  }

  @Test
  public void fromPropertiesSSLRootCert() {
    Mode mode = Mode.PREFER;
    String rootCert = "rootCert";
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    p.setProperty("db.ssl.mode", mode.toString());
    p.setProperty("db.ssl.rootCert", rootCert);
    Config c = Config.fromProperties("db", p);
    assertEquals(c.ssl(), Optional.of(SSL.apply(mode, new File(rootCert))));
  }

  @Test
  public void fromPropertiesFile() throws FileNotFoundException, IOException {
    Properties p = new Properties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);
    File file = File.createTempFile("test", "fromPropertiesFile");
    p.store(new FileOutputStream(file), "");

    Config c = Config.fromPropertiesFile("db", file.getAbsolutePath());
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
    Properties p = System.getProperties();
    p.setProperty("db.dataSourceSupplierClass", dataSourceSupplierClass);
    p.setProperty("db.host", host);
    p.setProperty("db.port", Integer.toString(port));
    p.setProperty("db.user", user);

    Config c = Config.fromSystemProperties("db");
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
