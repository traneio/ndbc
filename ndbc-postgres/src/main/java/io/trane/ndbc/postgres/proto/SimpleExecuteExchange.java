package io.trane.ndbc.postgres.proto;

import java.util.function.Function;

import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.Query;
import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;

public final class SimpleExecuteExchange implements Function<String, Exchange<Long>> {

  @Override
  public final Exchange<Long> apply(final String command) {
    return Exchange.send(new Query(command))
        .thenReceive(commandComplete)
        .thenWaitFor(ReadyForQuery.class);
  }

  private final PartialFunction<ServerMessage, Exchange<Long>> commandComplete = PartialFunction
      .when(CommandComplete.class, msg -> Exchange.value(msg.rows));
}
