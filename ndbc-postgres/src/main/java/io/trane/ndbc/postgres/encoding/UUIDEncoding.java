package io.trane.ndbc.postgres.encoding;

import java.util.UUID;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.UUIDValue;

final class UUIDEncoding extends Encoding<UUIDValue> {

  @Override
  public final Integer oid() {
    return Oid.UUID;
  }

  @Override
  public final Class<UUIDValue> valueClass() {
    return UUIDValue.class;
  }

  @Override
  public final String encodeText(final UUIDValue value) {
    return value.getUUID().toString();
  }

  @Override
  public final UUIDValue decodeText(final String value) {
    return new UUIDValue(UUID.fromString(value));
  }

  @Override
  public final void encodeBinary(final UUIDValue value, final BufferWriter b) {
    b.writeLong(value.getUUID().getMostSignificantBits());
    b.writeLong(value.getUUID().getLeastSignificantBits());
  }

  @Override
  public final UUIDValue decodeBinary(final BufferReader b) {
    return new UUIDValue(new UUID(b.readLong(), b.readLong()));
  }
}
