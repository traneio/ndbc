package io.trane.ndbc.proto;

import io.trane.future.Future;

public interface Channel {

  <T extends ClientMessage> Future<Void> send(final Marshaller<T> marshaller, final T msg);

  <T extends ServerMessage> Future<T> receive(final Unmarshaller<T> unmarshaller);

  Future<Void> close();
}
