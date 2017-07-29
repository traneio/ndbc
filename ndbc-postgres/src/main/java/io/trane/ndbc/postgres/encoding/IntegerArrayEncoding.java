package io.trane.ndbc.postgres.encoding;

import java.lang.reflect.Array;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.IntegerArrayValue;
import io.trane.ndbc.value.Value;

final class IntegerArrayEncoding extends Encoding<IntegerArrayValue> {

  private final IntegerEncoding integerEncoding;

  public IntegerArrayEncoding(IntegerEncoding integerEncoding) {
    this.integerEncoding = integerEncoding;
  }

  @Override
  public final Integer oid() {
    return Oid.INT4_ARRAY;
  }

  @Override
  public final Class<IntegerArrayValue> valueClass() {
    return IntegerArrayValue.class;
  }

  @Override
  public final String encodeText(final IntegerArrayValue value) {
    return null;
  }

  @Override
  public final IntegerArrayValue decodeText(final String value) {
    return null;
  }

  @Override
  public final void encodeBinary(final IntegerArrayValue value, final BufferWriter b) {
    Value<?>[] array = value.getValueArray();
    b.writeInt(1); // dimensions
    b.writeInt(0); // flags
    b.writeInt(Oid.INT4);
    b.writeInt(array.length);
    b.writeInt(1); // lbound
    for (Value<?> v : array) {
      if (v.isNull())
        b.writeInt(-1);
      else {
        b.writeInt(4);
        int lengthPos = b.writerIndex();
        b.writeInt(v.getInteger());
        // b.writeLength(lengthPos);
      }
    }
  }

  @Override
  public final IntegerArrayValue decodeBinary(final BufferReader b) {
    int dimensions = b.readInt();
    assert (dimensions <= 1);
    b.readInt(); // flags bit 0: 0=no-nulls, 1=has-nulls
    b.readInt(); // elementOid
    if (dimensions == 0)
      return IntegerArrayValue.EMPTY;
    else {
      int length = b.readInt();
      int lbound = b.readInt();
      assert (lbound == 1);

      Value<?>[] result = (Value<?>[]) Array.newInstance(Value.class, length);

      for (int i = 0; i < length; i++) {
        int elemLength = b.readInt();
        if (elemLength == -1)
          result[i] = Value.NULL;
        else {
          result[i] = integerEncoding.decodeBinary(b.readSlice(elemLength));
        }
      }
      return new IntegerArrayValue(result);
    }
  }
}
