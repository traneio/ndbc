package io.trane.ndbc.postgres.proto;

import io.trane.future.Future;

public class Channel {

  public Future<Message.BackendMessage> receive() {
    return null;
  }

  public Future<Void> send() {
    return null;
  }
}
