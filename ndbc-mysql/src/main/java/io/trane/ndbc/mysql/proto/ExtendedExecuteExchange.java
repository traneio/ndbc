package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.mysql.proto.Message.OkResponseMessage;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.util.PartialFunction;
import io.trane.ndbc.value.Value;

public final class ExtendedExecuteExchange implements BiFunction<String, List<Value<?>>, Exchange<Long>> {

  private final ExtendedExchange extendedExchange;

  public ExtendedExecuteExchange(ExtendedExchange extendedExchange) {
    this.extendedExchange = extendedExchange;
  }

  @Override
  public Exchange<Long> apply(final String command, final List<Value<?>> params) {
    return extendedExchange.apply(command, params, commandComplete);
  }

  private final Exchange<Long> commandComplete = Exchange.receive(
      PartialFunction.when(OkResponseMessage.class, msg -> Exchange.value(msg.affectedRows)));
}
