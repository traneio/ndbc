package io.trane.ndbc.mysql.proto;

import java.util.function.Function;

import io.trane.ndbc.mysql.proto.Message.OkResponseMessage;
import io.trane.ndbc.mysql.proto.Message.StatementCommand;
import io.trane.ndbc.mysql.proto.marshaller.Marshallers;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public final class SimpleExecuteExchange implements Function<String, Exchange<Long>> {

  private final Marshallers    marshallers;
  private final Exchange<Long> okResponse;

  public SimpleExecuteExchange(Marshallers marshallers, Unmarshallers unmarshallers) {
    this.marshallers = marshallers;
    this.okResponse = Exchange.receive(unmarshallers.serverResponse).flatMap(msg -> {
      if (msg instanceof OkResponseMessage)
        return Exchange.value(((OkResponseMessage) msg).affectedRows);
      else
        return Exchange.fail(msg.toString());
    });
  }

  @Override
  public Exchange<Long> apply(final String command) {
    return Exchange.send(marshallers.textCommand, new StatementCommand(command)).then(okResponse);
  }
}
