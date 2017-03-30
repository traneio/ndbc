package io.trane.ndbc.postgres.netty4;

import io.trane.future.Future;
import io.trane.ndbc.proto.ClientMessage;
import io.trane.ndbc.proto.ServerMessage;

public class Channel implements io.trane.ndbc.proto.Channel {

  @Override
  public Future<ServerMessage> receive() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Future<Void> send(ClientMessage msg) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Future<Void> close() {
    // TODO Auto-generated method stub
    return null;
  }

}
