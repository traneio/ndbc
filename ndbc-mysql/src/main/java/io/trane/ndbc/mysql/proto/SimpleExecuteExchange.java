package io.trane.ndbc.mysql.proto;

import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.OkResponseMessage;
import io.trane.ndbc.mysql.proto.Message.StatementCommand;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;

public final class SimpleExecuteExchange implements Function<String, Exchange<Long>> {

  @Override
  public Exchange<Long> apply(final String command) {
    return Exchange.send(new StatementCommand(command)).thenReceive(okResponse);
  }

  private final PartialFunction<ServerMessage, Exchange<Long>> okResponse = PartialFunction
      .when(OkResponseMessage.class, msg -> Exchange.value(msg.affectedRows));
}
