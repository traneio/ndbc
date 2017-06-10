package io.trane.ndbc;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConfigTest {

  private String dataSourceSupplierClass = "some.Class";
  private String host                    = "somehost";
  private int    port                    = 999;
  private String user                    = "some_user";

  @Test
  public void apply() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertEquals(c.dataSourceSupplierClass, dataSourceSupplierClass);
    assertEquals(c.host, host);
    assertEquals(c.port, port);
    assertEquals(c.user, user);
    assertEquals(c.charset, Charset.defaultCharset());
    assertFalse(c.password.isPresent());
    assertFalse(c.database.isPresent());
    assertFalse(c.poolMaxSize.isPresent());
    assertFalse(c.poolMaxWaiters.isPresent());
    assertFalse(c.poolValidationInterval.isPresent());
    assertFalse(c.encodingClasses.isPresent());
    assertFalse(c.nioThreads.isPresent());
  }

  @Test
  public void charset() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    Charset charset = Charset.availableCharsets().values().stream().filter(e -> e != Charset.defaultCharset())
        .collect(Collectors.toSet()).iterator().next();
    assertEquals(charset, c.charset(charset).charset);
  }

  @Test
  public void password() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    String password = "password";
    assertEquals(Optional.of(password), c.password(password).password);
  }

  @Test
  public void passwordOptionalEmpty() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    assertFalse(c.password(Optional.empty()).password.isPresent());
  }

  @Test
  public void passwordOptionalPresent() {
    Config c = Config.apply(dataSourceSupplierClass, host, port, user);
    String password = "password";
    assertEquals(Optional.of(password), c.password(Optional.of(password)).password);
  }
}
