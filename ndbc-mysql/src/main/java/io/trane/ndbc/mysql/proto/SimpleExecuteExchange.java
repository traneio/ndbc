package io.trane.ndbc.mysql.proto;

import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.StatementCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.proto.Exchange;

public final class SimpleExecuteExchange implements Function<String, Exchange<Long>> {

  private final Marshallers    marshallers;
  private final Exchange<Long> affectedRows;

  public SimpleExecuteExchange(Marshallers marshallers, Exchange<Long> affectedRows) {
    this.marshallers = marshallers;
    this.affectedRows = affectedRows;
  }

  @Override
  public Exchange<Long> apply(final String command) {
    return Exchange.send(marshallers.textCommand, new StatementCommand(command)).thenWaitFor(affectedRows);
  }
}
