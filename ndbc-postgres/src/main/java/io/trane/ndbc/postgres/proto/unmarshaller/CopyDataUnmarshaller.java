package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.CopyData;
import io.trane.ndbc.proto.BufferReader;;

public final class CopyDataUnmarshaller extends PostgresUnmarshaller<CopyData> {

  public CopyDataUnmarshaller(Charset charset) {
    super(charset);
  }

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == 'd';
  }

  @Override
  public final CopyData decode(final byte tpe, final BufferReader b) {
    return new CopyData(b.readBytes());
  }
}
