package io.trane.ndbc.mysql.proto.unmarshaller;

import java.math.BigInteger;
import java.util.List;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.Row;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.value.Value;

public class BinaryRowUnmarshaller extends MysqlUnmarshaller<Row> {

  private final List<Field>      fields;
  private final EncodingRegistry encoding;

  public BinaryRowUnmarshaller(final List<Field> fields, final EncodingRegistry encoding) {
    this.fields = fields;
    this.encoding = encoding;
  }

  @Override
  protected boolean acceptsHeader(final int header) {
    return header == TerminatorUnmarshaller.OK_BYTE;
  }

  @Override
  public Row decode(final int header, final PacketBufferReader p) {
    p.readByte(); // header
    final BigInteger nullBitmap = nullBitmap(p);
    final Value<?>[] values = new Value<?>[fields.size()];
    int i = 0;
    for (final Field field : fields) {
      if (nullBitmap.testBit(i + 2))
        values[i] = Value.NULL;
      else
        values[i] = encoding.decodeBinary(field, p);
      i++;
    }
    return new Row(values);
  }

  private final BigInteger nullBitmap(final PacketBufferReader p) {
    final byte[] nullBitmap = p.readBytes((fields.size() + 7 + 2) / 8);
    final int length = nullBitmap.length;
    final byte[] reversed = new byte[length];
    for (int i = 0; i < length; i++)
      reversed[length - 1 - i] = nullBitmap[i];
    return new BigInteger(reversed);
  }
}
