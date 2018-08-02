package io.trane.ndbc.postgres.proto.unmarshaller;

import io.trane.ndbc.postgres.proto.Message.ParameterDescription;
import io.trane.ndbc.proto.BufferReader;;

public final class ParameterDescriptionUnmarshaller extends PostgresUnmarshaller<ParameterDescription> {

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == 't';
  }

  @Override
  public final ParameterDescription decode(final byte tpe, final BufferReader b) {
    return new ParameterDescription(b.readInts(b.readShort()));
  }
}
