package io.trane.ndbc.mysql.proto.marshaller;

import java.util.List;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;
import io.trane.ndbc.mysql.proto.Message.ExecuteStatementCommand;
import io.trane.ndbc.mysql.proto.PacketBufferWriter;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.proto.Marshaller;
import io.trane.ndbc.value.Value;

public class ExecuteStatementCommandMarshaller implements Marshaller<ExecuteStatementCommand> {

  private final EncodingRegistry encoding;

  public ExecuteStatementCommandMarshaller(final EncodingRegistry encoding) {
    this.encoding = encoding;
  }

  @Override
  public void apply(final ExecuteStatementCommand command, final BufferWriter bw) {
    final PacketBufferWriter packet = new PacketBufferWriter(bw, 0);
    packet.writeByte((byte) 0x17); // status [0x17] COM_STMT_EXECUTE
    packet.writeUnsignedInt(command.statementId);
    packet.writeByte((byte) 0); // flags
    packet.writeUnsignedInt(1); // iterationCount
    if (!command.values.isEmpty()) {
      writeNullBitmap(packet, command.values);
      packet.writeByte((byte) 1); // new_params_bind_flag
      writeParameterTypes(packet, command.values);
      writeValues(packet, command.values);
    }
    packet.flush();
  }

  private void writeNullBitmap(final PacketBufferWriter packet, final List<Value<?>> values) {
    final byte[] bitmap = new byte[(values.size() + 7) / 8];
    for (int i = 0; i < values.size(); i++)
      if (values.get(i).isNull())
        bitmap[i / 8] = (byte) (bitmap[i / 8] | (1 << (i & 7)));
    packet.writeBytes(bitmap);
  }

  private void writeParameterTypes(final PacketBufferWriter packet, final List<Value<?>> values) {
    for (final Value<?> value : values)
      packet.writeUnsignedShort(encoding.fieldType(value));
  }

  private void writeValues(final PacketBufferWriter packet, final List<Value<?>> values) {
    for (final Value<?> value : values)
      encoding.encodeBinary(value, packet);
  }
}
