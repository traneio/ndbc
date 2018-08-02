package io.trane.ndbc.proto;

import java.util.Optional;

public interface UnmarshallerRegistry {

  ServerMessage apply(Optional<Class<? extends ClientMessage>> previousClientMessageClass,
      BufferReader bufferReader);
}
