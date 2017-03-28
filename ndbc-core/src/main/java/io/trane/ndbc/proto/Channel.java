package io.trane.ndbc.proto;

import io.trane.future.Future;

public interface Channel {

  Future<ServerMessage> receive();

  Future<Void> send(ClientMessage msg);

  Future<Void> close();
}
