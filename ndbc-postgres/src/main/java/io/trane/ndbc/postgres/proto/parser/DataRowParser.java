package io.trane.ndbc.postgres.proto.parser;

import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.proto.BufferReader;;

class DataRowParser {

  public DataRow decode(BufferReader b) {
    short columns = b.readShort();
    BufferReader[] values = new BufferReader[columns];
    for (short i = 0; i < columns; i++) {
      int length = b.readInt();
      if (length == -1)
        values[i] = null;
      else {
        BufferReader slice = b.readSlice(length);
        slice.retain();
        values[i] = slice;
      }
    }
    return new DataRow(values);
  }
}
