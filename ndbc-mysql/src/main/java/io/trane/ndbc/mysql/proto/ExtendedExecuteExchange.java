package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.mysql.proto.Message.*;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;
import io.trane.ndbc.value.Value;

public final class ExtendedExecuteExchange implements BiFunction<String, List<Value<?>>, Exchange<Long>>{

  @Override
  public Exchange<Long> apply(final String command, final List<Value<?>> params) {
    return Exchange.send(new PrepareStatementCommand(command)).thenReceive(okResponse);
  }

  private final PartialFunction<ServerMessage, Exchange<Long>> execute = PartialFunction
      .when(OkResponseMessage.class, msg -> Exchange.value(msg.affectedRows));

  private final PartialFunction<ServerMessage, Exchange<Long>> okResponse = PartialFunction
      .when(OkPrepareStatement.class, msg ->Exchange.send(
          new ExecuteStatementCommand(msg.statementId)).thenReceive(execute));
}
