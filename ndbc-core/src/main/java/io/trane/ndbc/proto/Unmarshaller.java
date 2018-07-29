package io.trane.ndbc.proto;

import java.util.Optional;

import io.trane.ndbc.util.Try;

public interface Unmarshaller {

	Try<Optional<ServerMessage>> decode(Optional<Class<? extends ClientMessage>> previousClientMessageClass,
			BufferReader bufferReader);
}
