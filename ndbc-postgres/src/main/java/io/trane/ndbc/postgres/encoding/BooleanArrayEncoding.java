package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.value.BooleanArrayValue;

final class BooleanArrayEncoding extends ArrayEncoding<Boolean, BooleanArrayValue> {

  private final BooleanEncoding booleanEncoding;
  private final Boolean[]       emptyArray = new Boolean[0];

  public BooleanArrayEncoding(BooleanEncoding booleanEncoding) {
    this.booleanEncoding = booleanEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.BOOL_ARRAY;
  }

  @Override
  public final Class<BooleanArrayValue> valueClass() {
    return BooleanArrayValue.class;
  }

  @Override
  protected Boolean[] newArray(int length) {
    return new Boolean[length];
  }

  @Override
  protected Boolean[] emptyArray() {
    return emptyArray;
  }

  @Override
  protected Encoding<Boolean, ?> itemEncoding() {
    return booleanEncoding;
  }

  @Override
  protected BooleanArrayValue box(Boolean[] value) {
    return new BooleanArrayValue(value);
  }

  @Override
  protected Boolean[] unbox(BooleanArrayValue value) {
    return value.getBooleanArray();
  }
}
