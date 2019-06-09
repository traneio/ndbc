package org.springframework.jdbc.datasource.embedded;

import org.springframework.util.Assert;

public class EmbeddedDatabaseConfigurerFactory {

  private EmbeddedDatabaseConfigurerFactory() {
  }

  public static EmbeddedDatabaseConfigurer getConfigurer(final EmbeddedDatabaseType type) throws IllegalStateException {
    Assert.notNull(type, "EmbeddedDatabaseType is required");
    try {
      switch (type) {
      case HSQL:
        return HsqlEmbeddedDatabaseConfigurer.getInstance();
      case H2:
        return H2EmbeddedDatabaseConfigurer.getInstance();
      case DERBY:
        return DerbyEmbeddedDatabaseConfigurer.getInstance();
      case POSTGRES:
        return PostgresEmbeddedDatabaseConfigurer.getInstance();
      default:
        throw new UnsupportedOperationException("Embedded database type [" + type + "] is not supported");
      }
    } catch (ClassNotFoundException | NoClassDefFoundError ex) {
      throw new IllegalStateException("Driver for test database type [" + type + "] is not available", ex);
    }
  }
}
