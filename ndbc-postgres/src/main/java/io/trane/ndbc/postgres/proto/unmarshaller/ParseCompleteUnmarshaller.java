package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.ParseComplete;
import io.trane.ndbc.proto.BufferReader;;

public final class ParseCompleteUnmarshaller extends PostgresUnmarshaller<ParseComplete> {

  private static final ParseComplete parseComplete = new ParseComplete();

  public ParseCompleteUnmarshaller(final Charset charset) {
    super(charset);
  }

  @Override
  protected boolean acceptsType(final byte tpe) {
    return tpe == '1';
  }

  @Override
  public final ParseComplete decode(final byte tpe, final BufferReader b) {
    return parseComplete;
  }
}
