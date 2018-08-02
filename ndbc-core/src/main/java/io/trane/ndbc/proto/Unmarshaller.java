package io.trane.ndbc.proto;

public interface Unmarshaller<T extends ServerMessage> {

  public T apply(BufferReader bufferReader);
}
