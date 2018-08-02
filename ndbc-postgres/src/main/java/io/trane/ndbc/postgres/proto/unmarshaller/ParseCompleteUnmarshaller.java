package io.trane.ndbc.postgres.proto.unmarshaller;

import io.trane.ndbc.postgres.proto.Message.ParseComplete;
import io.trane.ndbc.proto.BufferReader;;

public final class ParseCompleteUnmarshaller extends PostgresUnmarshaller<ParseComplete> {

  private static final ParseComplete parseComplete = new ParseComplete();

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == '1';
  }

  @Override
  public final ParseComplete decode(final byte tpe, final BufferReader b) {
    return parseComplete;
  }
}
