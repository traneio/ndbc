package io.trane.ndbc.proto;

public interface Marshaller<T extends ClientMessage> {

	public void apply(T msg, BufferWriter b);
}
