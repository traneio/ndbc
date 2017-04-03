package io.trane.ndbc.postgres.decoder;

import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.proto.BufferReader;;

class DataRowDecoder {

  public DataRow decode(BufferReader b) {
    short columns = b.readShort();
    byte[][] values = new byte[columns][];
    for (short i = 0; i < columns; i++) {
      int length = b.readInt();
      if (length == -1)
        values[i] = null;
      else
        values[i] = b.readBytes(length);
    }
    return new DataRow(values);
  }
}
