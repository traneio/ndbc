package io.trane.ndbc;

import io.trane.ndbc.value.Value;

public final class MysqlPreparedStatement extends PreparedStatement {

  public static MysqlPreparedStatement create(final PreparedStatement ps) {
    return new MysqlPreparedStatement(ps.query, ps.params);
  }

  public static MysqlPreparedStatement create(final String query) {
    return new MysqlPreparedStatement(query);
  }

  private MysqlPreparedStatement(final String query) {
    super(query);
  }

  private MysqlPreparedStatement(final String query, final Value<?>[] params) {
    super(query, params);
  }
}
