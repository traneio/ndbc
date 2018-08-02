package io.trane.ndbc.postgres.proto.unmarshaller;

import io.trane.ndbc.postgres.proto.Message.BackendKeyData;
import io.trane.ndbc.proto.BufferReader;;

public final class BackendKeyDataUnmarshaller extends PostgresUnmarshaller<BackendKeyData> {

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == 'K';
  }

  @Override
  public final BackendKeyData decode(final byte tpe, final BufferReader b) {
    return new BackendKeyData(b.readInt(), b.readInt());
  }
}
