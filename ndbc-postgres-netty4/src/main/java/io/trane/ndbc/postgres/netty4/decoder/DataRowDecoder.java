package io.trane.ndbc.postgres.netty4.decoder;

import static io.trane.ndbc.postgres.netty4.decoder.Read.*;
import io.netty.buffer.ByteBuf;
import io.trane.ndbc.postgres.proto.Message.DataRow;;

public class DataRowDecoder {

  public DataRow decode(ByteBuf buf) {
    short columns = buf.readShort();
    byte[][] values = new byte[columns][];
    for(short i = 0; i < columns; i ++) {
      int length = buf.readInt();
      if(length == -1)
        values[i] = null;
      else
        values[i] = bytes(buf, length);
    }
    return new DataRow(values);
  }
}
