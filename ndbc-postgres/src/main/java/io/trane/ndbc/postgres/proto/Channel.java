package io.trane.ndbc.postgres.proto;

import io.trane.future.Future;
import io.trane.ndbc.postgres.proto.Message.FrontendMessage;

public class Channel {

  public Future<Message.BackendMessage> receive() {
    return null;
  }

  public Future<Void> send(FrontendMessage msg) {
    return null;
  }
}
