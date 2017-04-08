package io.trane.ndbc.postgres.proto;

import java.nio.charset.Charset;

import io.trane.ndbc.ResultSet;
import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.proto.Exchange;

public class SimpleQueryExchange extends QueryExchange {

  public Exchange<ResultSet> apply(Charset charset, String query) {
    return Exchange.send(new Query(query))
        .then(readQueryResult(charset));
  }
}
