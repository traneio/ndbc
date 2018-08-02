package io.trane.ndbc.postgres.proto.unmarshaller;

import io.trane.ndbc.postgres.proto.Message.ParameterStatus;
import io.trane.ndbc.proto.BufferReader;;

public final class ParameterStatusUnmarshaller extends PostgresUnmarshaller<ParameterStatus> {

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == 'S';
  }

  @Override
  public final ParameterStatus decode(final byte tpe, final BufferReader b) {
    return new ParameterStatus(b.readCString(), b.readCString());
  }
}
