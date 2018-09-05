package io.trane.ndbc.postgres.proto;

import java.util.List;
import java.util.function.Function;

import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public final class SimpleQueryExchange implements Function<String, Exchange<List<Row>>> {

  private final QueryResultExchange queryResultExchange;
  private final Marshallers         marshallers;
  private final Unmarshallers       unmarshallers;

  public SimpleQueryExchange(final QueryResultExchange queryResultExchange, final Marshallers marshallers,
      final Unmarshallers unmarshallers) {
    this.queryResultExchange = queryResultExchange;
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
  }

  @Override
  public final Exchange<List<Row>> apply(final String query) {
    return Exchange.send(marshallers.query, new Query(query)).then(queryResultExchange.apply())
        .thenWaitFor(unmarshallers.readyForQuery);
  }
}
