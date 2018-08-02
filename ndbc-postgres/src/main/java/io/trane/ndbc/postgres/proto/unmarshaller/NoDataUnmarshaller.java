package io.trane.ndbc.postgres.proto.unmarshaller;

import io.trane.ndbc.postgres.proto.Message.NoData;
import io.trane.ndbc.proto.BufferReader;;

public final class NoDataUnmarshaller extends PostgresUnmarshaller<NoData> {

  private static final NoData noData = new NoData();

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == 'n';
  }

  @Override
  public final NoData decode(final byte tpe, final BufferReader b) {
    return noData;
  }
}
