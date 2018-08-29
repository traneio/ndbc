package io.trane.ndbc.mysql.proto.unmarshaller;

import java.util.List;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.TextRow;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.value.Value;

public class TextRowUnmarshaller extends MysqlUnmarshaller<TextRow> {

  private final List<Field>      fields;
  private final EncodingRegistry encoding;

  public TextRowUnmarshaller(List<Field> fields, EncodingRegistry encoding) {
    this.fields = fields;
    this.encoding = encoding;
  }

  @Override
  public TextRow decode(int header, PacketBufferReader p) {
    int size = fields.size();
    final Value<?>[] values = new Value<?>[size];
    for (int i = 0; i < size; i++)
      values[i] = encoding.decodeText(fields.get(i).fieldType, p);
    return new TextRow(values);
  }
}
