package io.trane.ndbc.postgres.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.NoData;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.proto.ServerMessage;
import io.trane.ndbc.util.PartialFunction;
import io.trane.ndbc.value.Value;

public final class ExtendedExecuteExchange
    implements BiFunction<String, List<Value<?>>, Exchange<Integer>> {

  private final ExtendedExchange extendedExchange;

  public ExtendedExecuteExchange(final ExtendedExchange extendedExchange) {
    super();
    this.extendedExchange = extendedExchange;
  }

  public final Exchange<Integer> apply(final String query, final List<Value<?>> params) {
    return extendedExchange.apply(query, params,
        Exchange.receive(commandComplete.orElse(noDataAndCommandComplete)));
  }

  private final PartialFunction<ServerMessage, Exchange<Integer>> commandComplete          = PartialFunction
      .when(CommandComplete.class, msg -> Exchange.value(msg.rows));

  private final PartialFunction<ServerMessage, Exchange<Integer>> noDataAndCommandComplete = PartialFunction
      .when(NoData.class, msg -> Exchange.receive(commandComplete));
}
