package io.trane.ndbc.postgres.proto;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;

public class ExtendedExecuteExchange {

  private final ExtendedExchange extendedExchange;

  public ExtendedExecuteExchange(ExtendedExchange extendedExchange) {
    super();
    this.extendedExchange = extendedExchange;
  }

  public final Exchange<Integer> apply(PreparedStatement ps) {
    return extendedExchange.apply(ps, Exchange.receive(commandComplete));
  }

  private final PartialFunction<ServerMessage, Exchange<Integer>> commandComplete = PartialFunction.when(
      CommandComplete.class, msg -> Exchange.value(msg.rows));
}
