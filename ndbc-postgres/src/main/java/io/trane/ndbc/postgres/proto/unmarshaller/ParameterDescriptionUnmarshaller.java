package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.postgres.proto.Message.ParameterDescription;
import io.trane.ndbc.proto.BufferReader;;

public final class ParameterDescriptionUnmarshaller extends PostgresUnmarshaller<ParameterDescription> {

  public ParameterDescriptionUnmarshaller(final Charset charset) {
    super(charset);
  }

  @Override
  protected boolean acceptsType(final byte tpe) {
    return tpe == 't';
  }

  @Override
  public final ParameterDescription decode(final byte tpe, final BufferReader b) {
    return new ParameterDescription(b.readInts(b.readShort()));
  }
}
