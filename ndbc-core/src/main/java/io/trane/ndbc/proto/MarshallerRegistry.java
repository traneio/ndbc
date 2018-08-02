package io.trane.ndbc.proto;

public interface MarshallerRegistry {

	void encode(ClientMessage msg, BufferWriter bufferWriter);
}
