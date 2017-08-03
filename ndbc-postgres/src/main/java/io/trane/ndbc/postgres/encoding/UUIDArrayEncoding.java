package io.trane.ndbc.postgres.encoding;

import java.util.UUID;

import io.trane.ndbc.value.UUIDArrayValue;

final class UUIDArrayEncoding extends ArrayEncoding<UUID, UUIDArrayValue> {

  private final UUIDEncoding uuidEncoding;
  private final UUID[]       emptyArray = new UUID[0];

  public UUIDArrayEncoding(UUIDEncoding uuidEncoding) {
    this.uuidEncoding = uuidEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.UUID_ARRAY;
  }

  @Override
  public final Class<UUIDArrayValue> valueClass() {
    return UUIDArrayValue.class;
  }

  @Override
  protected UUID[] newArray(int length) {
    return new UUID[length];
  }

  @Override
  protected UUID[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<UUID, ?> itemEncoding() {
    return uuidEncoding;
  }

  @Override
  protected UUIDArrayValue box(UUID[] value) {
    return new UUIDArrayValue(value);
  }

  @Override
  protected UUID[] unbox(UUIDArrayValue value) {
    return value.getUUIDArray();
  }
}
