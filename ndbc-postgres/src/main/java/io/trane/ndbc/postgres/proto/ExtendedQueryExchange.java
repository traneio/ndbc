package io.trane.ndbc.postgres.proto;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;
import io.trane.ndbc.proto.Exchange;

public final class ExtendedQueryExchange {

  private final QueryResultExchange queryResultExchange;
  private final ExtendedExchange extendedExchange;

  public ExtendedQueryExchange(final QueryResultExchange queryResultExchange, final ExtendedExchange extendedExchange) {
    super();
    this.queryResultExchange = queryResultExchange;
    this.extendedExchange = extendedExchange;
  }

  public final Exchange<ResultSet> apply(final PreparedStatement ps) {
    return extendedExchange.apply(ps, queryResultExchange.apply());
  }
}
