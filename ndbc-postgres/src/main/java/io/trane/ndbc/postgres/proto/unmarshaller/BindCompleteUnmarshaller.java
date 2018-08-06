package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.BindComplete;
import io.trane.ndbc.proto.BufferReader;;

public final class BindCompleteUnmarshaller extends PostgresUnmarshaller<BindComplete> {

  private static final BindComplete bindComplete = new BindComplete();

  public BindCompleteUnmarshaller(Charset charset) {
    super(charset);
  }

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == '2';
  }

  @Override
  public final BindComplete decode(final byte tpe, final BufferReader b) {
    return bindComplete;
  }
}
