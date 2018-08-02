package io.trane.ndbc.postgres.proto.unmarshaller;

import io.trane.ndbc.postgres.proto.Message.BindComplete;
import io.trane.ndbc.proto.BufferReader;;

public final class BindCompleteUnmarshaller extends PostgresUnmarshaller<BindComplete> {

  private static final BindComplete bindComplete = new BindComplete();

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == '2';
  }

  @Override
  public final BindComplete decode(final byte tpe, final BufferReader b) {
    return bindComplete;
  }
}
