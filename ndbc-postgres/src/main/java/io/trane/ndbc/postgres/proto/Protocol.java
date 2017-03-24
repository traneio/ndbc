package io.trane.ndbc.postgres.proto;

import io.trane.future.Future;

public class Protocol {

  private final Channel channel;

  public Protocol(Channel channel) {
    super();
    this.channel = channel;
  }

  public Future<Void> authenticate(String user, String password) {
    return null;
  }

}
