package io.trane.ndbc.proto;

import java.util.Optional;

import io.trane.ndbc.util.Try;

public interface Unmarshaller {

  Optional<Try<ServerMessage>> decode(Optional<Class<? extends ClientMessage>> previousClientMessageClass,
			BufferReader bufferReader);
}
