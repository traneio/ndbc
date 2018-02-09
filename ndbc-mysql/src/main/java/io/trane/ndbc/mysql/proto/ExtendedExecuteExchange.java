package io.trane.ndbc.mysql.proto;

import java.util.List;
import java.util.function.BiFunction;

import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class ExtendedExecuteExchange implements BiFunction<String, List<Value<?>>, Exchange<Long>>{

  @Override
  public Exchange<Long> apply(String t, List<Value<?>> u) {
    return null;
  }
}
