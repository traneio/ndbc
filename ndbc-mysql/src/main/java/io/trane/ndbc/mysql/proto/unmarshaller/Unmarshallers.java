package io.trane.ndbc.mysql.proto.unmarshaller;

import java.nio.charset.Charset;
import java.util.List;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.Row;

public class Unmarshallers {

  private final EncodingRegistry encoding;

  public final ColumnCountUnmarshaller columnCount;
  public final FieldUnmarshaller       field;
  public final HandshakeUnmarshaller   handshake;
  public final TerminatorUnmarshaller  terminator;
  public final PrepareOkUnmarshaller   prepareOk;

  public Unmarshallers(final Charset charset, final EncodingRegistry encoding) {
    this.encoding = encoding;
    this.columnCount = new ColumnCountUnmarshaller();
    this.field = new FieldUnmarshaller(charset);
    this.handshake = new HandshakeUnmarshaller(charset);
    this.terminator = new TerminatorUnmarshaller(charset);
    this.prepareOk = new PrepareOkUnmarshaller();
  }

  public final MysqlUnmarshaller<Row> row(final List<Field> fields, final boolean binary) {
    if (!binary)
      return new TextRowUnmarshaller(fields, encoding);
    else
      return new BinaryRowUnmarshaller(fields, encoding);
  }
}
