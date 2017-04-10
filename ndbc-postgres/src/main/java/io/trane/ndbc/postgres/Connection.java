package io.trane.ndbc.postgres;

import java.util.Optional;
import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;
import io.trane.ndbc.postgres.proto.ExtendedExecuteExchange;
import io.trane.ndbc.postgres.proto.ExtendedQueryExchange;
import io.trane.ndbc.postgres.proto.Message.BackendKeyData;
import io.trane.ndbc.postgres.proto.SimpleExecuteExchange;
import io.trane.ndbc.postgres.proto.SimpleQueryExchange;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.proto.Exchange;

public class Connection implements io.trane.ndbc.Connection {

  private final Channel channel;
  private final Optional<BackendKeyData> backendKeyData;
  private final SimpleQueryExchange simpleQueryExchange;
  private final SimpleExecuteExchange simpleExecuteExchange;
  private final ExtendedQueryExchange extendedQueryExchange;
  private final ExtendedExecuteExchange extendedExecuteExchange;

  public Connection(Channel channel, Optional<BackendKeyData> backendKeyData, SimpleQueryExchange simpleQueryExchange,
      SimpleExecuteExchange simpleExecuteExchange, ExtendedQueryExchange extendedQueryExchange,
      ExtendedExecuteExchange extendedExecuteExchange) {
    super();
    this.channel = channel;
    this.backendKeyData = backendKeyData;
    this.simpleQueryExchange = simpleQueryExchange;
    this.simpleExecuteExchange = simpleExecuteExchange;
    this.extendedQueryExchange = extendedQueryExchange;
    this.extendedExecuteExchange = extendedExecuteExchange;
  }

  @Override
  public Future<ResultSet> query(String query) {
    return simpleQueryExchange.apply(query).run(channel);
  }

  @Override
  public Future<Integer> execute(String command) {
    return simpleExecuteExchange.apply(command).run(channel);
  }

  @Override
  public Future<ResultSet> query(PreparedStatement query) {
    return extendedQueryExchange.apply(query).run(channel);
  }

  @Override
  public Future<Integer> execute(PreparedStatement command) {
    return extendedExecuteExchange.apply(command).run(channel);
  }

  @Override
  public <R> Future<R> withTransaction(Supplier<Future<R>> sup) {
    return null;
  }

  @Override
  public Future<Boolean> isValid() {
    return query("SELECT 1").map(r -> true).rescue(e -> Future.FALSE);
  }

  @Override
  public Future<Void> close() {
    return Exchange.close().run(channel);
  }
}
