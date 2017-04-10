package io.trane.ndbc.postgres.proto;

import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;

public class SimpleExecuteExchange {

  public Exchange<Integer> apply(String command) {
    return Exchange.send(new Query(command))
        .thenReceive(commandComplete)
        .thenWaitFor(ReadyForQuery.class);
  }

  private final PartialFunction<ServerMessage, Exchange<Integer>> commandComplete = PartialFunction.when(
      CommandComplete.class, msg -> Exchange.value(msg.rows));
}
