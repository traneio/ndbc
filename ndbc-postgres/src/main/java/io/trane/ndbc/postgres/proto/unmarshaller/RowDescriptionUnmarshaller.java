package io.trane.ndbc.postgres.proto.unmarshaller;

import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.proto.BufferReader;

public final class RowDescriptionUnmarshaller extends PostgresUnmarshaller<RowDescription> {

  @Override
  protected boolean acceptsType(byte tpe) {
    return tpe == 'T';
  }

  @Override
  public final RowDescription decode(final byte tpe, final BufferReader b) {
    final short size = b.readShort();
    final RowDescription.Field[] fields = new RowDescription.Field[size];
    for (int i = 0; i < size; i++)
      fields[i] = new RowDescription.Field(b.readCString(), b.readInt(), b.readShort(), b.readInt(),
          b.readShort(), b.readInt(), b.readShort());
    return new RowDescription(fields);
  }
}
