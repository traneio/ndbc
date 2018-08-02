package io.trane.ndbc.postgres.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.NoData;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExecuteExchange implements BiFunction<String, List<Value<?>>, Exchange<Long>> {

  private final ExtendedExchange extendedExchange;
  private final Exchange<Long>   readResult;

  public ExtendedExecuteExchange(final ExtendedExchange extendedExchange, final Unmarshallers unmarshallers) {
    this.extendedExchange = extendedExchange;
    this.readResult = Exchange
        .receive(unmarshallers.noData.orElse(unmarshallers.commandComplete)).flatMap(msg -> {
          if (msg instanceof NoData)
            return Exchange.receive(unmarshallers.commandComplete).map(r -> r.rows);
          else
            return Exchange.value(((CommandComplete) msg).rows);
        });
  }

  @Override
  public final Exchange<Long> apply(final String query, final List<Value<?>> params) {
    return extendedExchange.apply(query, params, readResult);
  }
}
