package io.trane.ndbc.postgres.proto;

import io.trane.ndbc.ResultSet;
import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.proto.Exchange;

public class SimpleQueryExchange extends QueryExchange {

  public Exchange<ResultSet> apply(String query) {
    return Exchange.send(new Query(query))
        .then(readQueryResult());
  }
}
