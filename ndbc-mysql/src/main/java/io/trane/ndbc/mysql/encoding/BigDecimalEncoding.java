package io.trane.ndbc.mysql.encoding;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Set;

import io.trane.ndbc.mysql.proto.FieldType;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.BigDecimalValue;

final class BigDecimalEncoding extends Encoding<BigDecimal, BigDecimalValue> {

  @Override
  public Key key() {
    return key(FieldType.NEW_DECIMAL);
  }

  @Override
  public Set<Key> additionalKeys() {
    return Collections.toImmutableSet(key(FieldType.DECIMAL), key(FieldType.NUMERIC));
  }

  @Override
  public final Class<BigDecimalValue> valueClass() {
    return BigDecimalValue.class;
  }

  @Override
  public final BigDecimal decodeText(final String value, final Charset charset) {
    return new BigDecimal(value);
  }

  @Override
  public final void encodeBinary(final BigDecimal value, final PacketBufferWriter b, final Charset charset) {
    b.writeLengthCodedString(charset, value.toString());
  }

  @Override
  public final BigDecimal decodeBinary(final PacketBufferReader b, final Key key, final Charset charset) {
    return new BigDecimal(b.readLengthCodedString(charset));
  }

  @Override
  protected BigDecimalValue box(final BigDecimal value) {
    return new BigDecimalValue(value);
  }
}
