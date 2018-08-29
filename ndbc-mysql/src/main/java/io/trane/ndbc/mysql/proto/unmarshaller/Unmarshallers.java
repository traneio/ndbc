package io.trane.ndbc.mysql.proto.unmarshaller;

import java.nio.charset.Charset;
import java.util.List;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;
import io.trane.ndbc.mysql.proto.Message.Field;

public class Unmarshallers {

  private final EncodingRegistry encoding;

  public final ColumnCountUnmarshaller columnCount;
  public final FieldUnmarshaller       field;
  public final HandshakeUnmarshaller   handshake;
  public final TerminatorUnmarshaller  terminator;
  public final PrepareOkUnmarshaller   prepareOk;

  public Unmarshallers(Charset charset, EncodingRegistry encoding) {
    this.encoding = encoding;
    this.columnCount = new ColumnCountUnmarshaller();
    this.field = new FieldUnmarshaller(charset);
    this.handshake = new HandshakeUnmarshaller(charset);
    this.terminator = new TerminatorUnmarshaller(charset);
    this.prepareOk = new PrepareOkUnmarshaller();
  }

  public final TextRowUnmarshaller textRow(List<Field> fields) {
    return new TextRowUnmarshaller(fields, encoding);
  }
}
