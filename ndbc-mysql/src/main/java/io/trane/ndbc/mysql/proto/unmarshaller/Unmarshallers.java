package io.trane.ndbc.mysql.proto.unmarshaller;

import java.nio.charset.Charset;

public class Unmarshallers {

  private final Charset charset;

  public final ColumnCountUnmarshaller columnCount;
  public final FieldUnmarshaller       field;
  public final HandshakeUnmarshaller   handshake;
  public final TerminatorUnmarshaller  terminator;

  public Unmarshallers(Charset charset) {
    this.charset = charset;
    this.columnCount = new ColumnCountUnmarshaller();
    this.field = new FieldUnmarshaller(charset);
    this.handshake = new HandshakeUnmarshaller(charset);
    this.terminator = new TerminatorUnmarshaller(charset);
  }

  public final TextRowUnmarshaller textRow(int columnCount) {
    return new TextRowUnmarshaller(columnCount, charset);
  }
}
