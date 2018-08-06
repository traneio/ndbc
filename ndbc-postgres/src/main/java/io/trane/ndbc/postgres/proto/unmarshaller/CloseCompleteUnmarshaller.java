package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.CloseComplete;
import io.trane.ndbc.proto.BufferReader;;

public final class CloseCompleteUnmarshaller extends PostgresUnmarshaller<CloseComplete> {

  private static final CloseComplete closeComplete = new CloseComplete();

  public CloseCompleteUnmarshaller(Charset charset) {
    super(charset);
  }

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == '3';
  }

  @Override
  public final CloseComplete decode(final byte tpe, final BufferReader b) {
    return closeComplete;
  }
}
