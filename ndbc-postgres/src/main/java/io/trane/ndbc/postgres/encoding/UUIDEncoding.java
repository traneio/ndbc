package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.util.UUID;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.UUIDValue;

final class UUIDEncoding extends Encoding<UUID, UUIDValue> {

  public UUIDEncoding(final Charset charset) {
    super(charset);
  }

  @Override
  public final Integer oid() {
    return Oid.UUID;
  }

  @Override
  public final Class<UUIDValue> valueClass() {
    return UUIDValue.class;
  }

  @Override
  public final UUID decodeText(final String value) {
    return UUID.fromString(value);
  }

  @Override
  public final void encodeBinary(final UUID value, final BufferWriter b) {
    b.writeLong(value.getMostSignificantBits());
    b.writeLong(value.getLeastSignificantBits());
  }

  @Override
  public final UUID decodeBinary(final BufferReader b) {
    return new UUID(b.readLong(), b.readLong());
  }

  @Override
  protected UUIDValue box(final UUID value) {
    return new UUIDValue(value);
  }
}
