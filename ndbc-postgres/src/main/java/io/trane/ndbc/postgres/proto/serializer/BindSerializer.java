package io.trane.ndbc.postgres.proto.serializer;

import io.trane.ndbc.postgres.proto.Message.Bind;
import io.trane.ndbc.proto.BufferWriter;

public class BindSerializer {

  public final void encode(Bind msg, BufferWriter b) {
    b.writeByte((byte) 'B');
    b.writeCString(msg.destinationPortalName);
    b.writeCString(msg.sourcePreparedStatementName);
    b.writeShort((short) msg.parameterFormatCodes.length);
    for (short code : msg.parameterFormatCodes)
      b.writeShort(code);
    b.writeShort((short) msg.fields.length);
    for (byte[] field : msg.fields)
      if (field == null)
        b.writeInt(-1);
      else {
        b.writeInt(field.length);
        b.writeBytes(field);
      }
    b.writeShort((short) msg.resultColumnFormatCodes.length);
    for (short code : msg.resultColumnFormatCodes)
      b.writeShort(code);
    b.writeLength(1);
  }

}
