package io.trane.ndbc.postgres.decoder;

import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.proto.BufferReader;

public class RowDescriptionDecoder {

  public final RowDescription decode(BufferReader b) {
    short size = b.readShort();
    RowDescription.Field[] fields = new RowDescription.Field[size];
    for (int i = 0; i < size; i++)
      fields[i] = new RowDescription.Field(b.readCString(), b.readInt(), b.readShort(), b.readInt(), b.readShort(),
          b.readInt(), b.readShort());
    return new RowDescription(fields);
  }

}
