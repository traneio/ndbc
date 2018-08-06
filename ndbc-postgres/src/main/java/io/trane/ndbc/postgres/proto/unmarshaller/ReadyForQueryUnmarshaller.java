package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.ReadyForQuery;
import io.trane.ndbc.proto.BufferReader;

public final class ReadyForQueryUnmarshaller extends PostgresUnmarshaller<ReadyForQuery> {

  public ReadyForQueryUnmarshaller(Charset charset) {
    super(charset);
  }

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == 'Z';
  }

  @Override
  public final ReadyForQuery decode(final byte tpe, final BufferReader b) {
    return new ReadyForQuery(b.readByte());
  }
}
