package io.trane.ndbc.mysql.proto;

import java.util.List;

import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExchange {

  private final Marshallers              marshallers;
  private final PrepareStatementExchange prepareStatement;

  public ExtendedExchange(Marshallers marshallers, PrepareStatementExchange prepareStatement) {
    this.marshallers = marshallers;
    this.prepareStatement = prepareStatement;
  }

  public final <T> Exchange<T> apply(final String query, final List<Value<?>> params, final Exchange<T> readResult) {
    return prepareStatement.apply(query, params).flatMap(
        id -> Exchange.send(marshallers.executeStatementCommand, new ExecuteStatementCommand(id, params, false))
            .then(readResult));
  }
}
