package io.trane.ndbc.postgres.proto;

import java.util.function.Function;

import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.postgres.proto.marshaller.Marshallers;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public final class SimpleExecuteExchange implements Function<String, Exchange<Long>> {

  private final Marshallers   marshallers;
  private final Unmarshallers unmarshallers;

  public SimpleExecuteExchange(final Marshallers marshallers, final Unmarshallers unmarshallers) {
    this.marshallers = marshallers;
    this.unmarshallers = unmarshallers;
  }

  @Override
  public final Exchange<Long> apply(final String command) {
    return Exchange.send(marshallers.query, new Query(command))
        .then(Exchange.receive(unmarshallers.commandComplete).map(c -> c.rows))
        .thenWaitFor(unmarshallers.readyForQuery);
  }
}
