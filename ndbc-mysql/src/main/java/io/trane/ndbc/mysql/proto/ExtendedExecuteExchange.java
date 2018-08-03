package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.mysql.proto.Message.OkResponseMessage;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExecuteExchange implements BiFunction<String, List<Value<?>>, Exchange<Long>> {

  private final ExtendedExchange extendedExchange;
  private final Exchange<Long>   commandComplete;

  public ExtendedExecuteExchange(ExtendedExchange extendedExchange, final Unmarshallers unmarshallers) {
    this.extendedExchange = extendedExchange;
    this.commandComplete = Exchange.receive(unmarshallers.serverResponse).flatMap(msg -> {
      if (msg instanceof OkResponseMessage)
        return Exchange.value(((OkResponseMessage) msg).affectedRows);
      else
        return Exchange.fail(msg.toString());
    });
  }

  @Override
  public Exchange<Long> apply(final String command, final List<Value<?>> params) {
    return extendedExchange.apply(command, params, commandComplete);
  }

}
