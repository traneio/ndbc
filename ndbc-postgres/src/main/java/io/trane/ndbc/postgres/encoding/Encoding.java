package io.trane.ndbc.postgres.encoding;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.Value;

public abstract class Encoding<T, V extends Value<T>> {

	public void encode(final Format format, final V value, final BufferWriter writer) {
		if (format == Format.TEXT)
			writer.writeString(encodeText(unbox(value)));
		else
			encodeBinary(unbox(value), writer);
	}

	public V decode(final Format format, final BufferReader reader) {
		if (format == Format.TEXT)
			return box(decodeText(reader.readString()));
		else
			return box(decodeBinary(reader));
	}

	public abstract Integer oid();

	private static Set<Integer> emptyOids = Collections.unmodifiableSet(new HashSet<>());

	public Set<Integer> additionalOids() {
		return emptyOids;
	}

	public abstract Class<V> valueClass();

	protected abstract V box(T value);

	protected abstract T unbox(V value);

	protected abstract String encodeText(T value);

	protected abstract T decodeText(String value);

	protected abstract void encodeBinary(T value, BufferWriter b);

	protected abstract T decodeBinary(BufferReader b);
}
