package io.trane.ndbc.mysql.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.Message.BinaryRow;
import io.trane.ndbc.mysql.proto.PacketBufferReader;

public class BinaryRowUnmarshaller extends MysqlUnmarshaller<BinaryRow> {

  private final int columnCount;
  private final Charset charset;

  public BinaryRowUnmarshaller(int columnCount, Charset charset) {
    this.columnCount = columnCount;
    this.charset = charset;
  }

  @Override
  protected BinaryRow decode(int header, PacketBufferReader packet) {
    final String[] values = new String[columnCount];
    for (int i = 0; i < values.length; i++)
      values[i] = packet.readLengthCodedString(charset);
    return new BinaryRow(values);
  }
}
