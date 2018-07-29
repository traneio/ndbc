package io.trane.ndbc.proto;

public interface Marshaller {

  void encode(ClientMessage msg, BufferWriter bufferWriter);
}
