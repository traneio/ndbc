package io.trane.ndbc.mysql.proto.unmarshaller;

import io.trane.ndbc.mysql.proto.Message.EofResponseMessage;
import io.trane.ndbc.mysql.proto.Message.ErrorResponseMessage;
import io.trane.ndbc.mysql.proto.Message.OkResponseMessage;
import io.trane.ndbc.mysql.proto.Message.ServerResponseMessage;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.proto.BufferReader;

public class ServerResponseUnmarshaller {
	public final static int OK_BYTE = 0x00;
	public final static int ERROR_BYTE = 0xFF;
	public final static int EOF_BYTE = 0xFE;

	public final ServerResponseMessage decode(final BufferReader br) {
		final PacketBufferReader packet = new PacketBufferReader(br);
		final int responseType = packet.readByte() & 0xFF;
		switch (responseType) {
			case (OK_BYTE) :
				return decodeOk(packet);
			case (ERROR_BYTE) :
				return decodeError(packet);
			case (EOF_BYTE) :
				return decodeEof(packet);
			default :
				throw new UnsupportedOperationException("Unsupported response from server " + responseType);
		}
	}

	public static EofResponseMessage decodeEof(final PacketBufferReader packet) {
		packet.readByte();
		return new EofResponseMessage(packet.readUnsignedShort(), packet.readUnsignedShort());
	}

	public static OkResponseMessage decodeOk(final PacketBufferReader packet) {
		final long affectedRows = packet.readVariableLong();
		final long insertedId = packet.readVariableLong();
		final int serverStatus = packet.readUnsignedShort();
		final int warningCount = packet.readUnsignedShort();
		final String message = new String(packet.readBytes());
		return new OkResponseMessage(affectedRows, insertedId, serverStatus, warningCount, message);
	}

	public static ErrorResponseMessage decodeError(final PacketBufferReader packet) {
		final byte[] bytes = packet.readBytes();
		final String errorMessage = new String(bytes);
		return new ErrorResponseMessage(errorMessage);
	}

}
