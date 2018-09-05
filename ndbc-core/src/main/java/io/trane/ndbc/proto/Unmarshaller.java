package io.trane.ndbc.proto;

import java.util.Optional;

public interface Unmarshaller<T extends ServerMessage> {

  public Optional<T> apply(BufferReader bufferReader);
}
