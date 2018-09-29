package io.trane.ndbc.mysql.proto.unmarshaller;

import java.util.List;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.Row;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.value.Value;

public class TextRowUnmarshaller extends MysqlUnmarshaller<Row> {

  private final List<Field>      fields;
  private final EncodingRegistry encoding;

  public TextRowUnmarshaller(final List<Field> fields, final EncodingRegistry encoding) {
    this.fields = fields;
    this.encoding = encoding;
  }

  @Override
  protected boolean acceptsHeader(final int header, final int readableBytes) {
    return !TerminatorUnmarshaller.isTerminator(header, readableBytes);
  }

  @Override
  public Row decode(final PacketBufferReader p) {
    final Value<?>[] values = new Value<?>[fields.size()];
    int i = 0;
    for (final Field field : fields) {
      values[i] = encoding.decodeText(field, p);
      i++;
    }
    return new Row(values);
  }
}
