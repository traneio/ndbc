package io.trane.ndbc.postgres.proto;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;
import io.trane.ndbc.proto.Exchange;

public class ExtendedQueryExchange {

  private final QueryResultExchange queryResultExchange;
  private final ExtendedExchange extendedExchange;

  public ExtendedQueryExchange(QueryResultExchange queryResultExchange,
      ExtendedExchange extendedExchange) {
    super();
    this.queryResultExchange = queryResultExchange;
    this.extendedExchange = extendedExchange;
  }

  public final Exchange<ResultSet> apply(PreparedStatement ps) {
    return extendedExchange.apply(ps, queryResultExchange.apply());
  }
}
