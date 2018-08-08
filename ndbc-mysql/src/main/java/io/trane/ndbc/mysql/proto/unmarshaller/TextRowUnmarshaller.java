package io.trane.ndbc.mysql.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.Message.TextRow;
import io.trane.ndbc.mysql.proto.PacketBufferReader;

public class TextRowUnmarshaller extends MysqlUnmarshaller<TextRow> {

  private final int     columnCount;
  private final Charset charset;

  public TextRowUnmarshaller(int columnCount, Charset charset) {
    this.columnCount = columnCount;
    this.charset = charset;
  }

  @Override
  public TextRow decode(int header, PacketBufferReader p) {
    final String[] values = new String[columnCount];
    for (int i = 0; i < values.length; i++)
      values[i] = p.readLengthCodedString(charset);
    return new TextRow(values);
  }
}
