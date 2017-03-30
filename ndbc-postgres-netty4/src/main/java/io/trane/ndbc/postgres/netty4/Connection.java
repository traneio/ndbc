package io.trane.ndbc.postgres.netty4;

import java.util.function.Supplier;

import io.trane.future.Future;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.ResultSet;
import io.trane.ndbc.proto.Channel;
import io.trane.ndbc.postgres.proto.Message.BackendKeyData;

public class Connection implements io.trane.ndbc.datasource.Connection {

  private final Channel channel;
  private final BackendKeyData backendKeyData;

  public Connection(Channel channel, BackendKeyData backendKeyData) {
    super();
    this.channel = channel;
    this.backendKeyData = backendKeyData;
  }

  @Override
  public Future<ResultSet> query(String query) {
    return null;
  }

  @Override
  public Future<Integer> execute(String query) {
    return null;
  }

  @Override
  public Future<ResultSet> query(PreparedStatement query) {
    return null;
  }

  @Override
  public Future<Integer> execute(PreparedStatement query) {
    return null;
  }

  @Override
  public <R> Future<R> withTransaction(Supplier<Future<R>> sup) {
    return null;
  }
}
