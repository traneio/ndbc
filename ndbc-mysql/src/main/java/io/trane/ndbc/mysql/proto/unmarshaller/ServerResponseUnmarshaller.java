package io.trane.ndbc.mysql.proto.unmarshaller;

import static io.trane.ndbc.mysql.proto.Message.*;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.proto.BufferReader;

public class ServerResponseUnmarshaller {
	public final static int OK_BYTE = 0x00;
	public final static int ERROR_BYTE = 0xFF;
	public final static int EOF_BYTE = 0xFE;

	public final ServerResponseMessage decode(final BufferReader br) {
		PacketBufferReader packet = new PacketBufferReader(br);
		int responseType = packet.readByte() & 0xFF;
		switch (responseType) {
			case (OK_BYTE) :
				return decodeOk(packet);
			case (ERROR_BYTE) :
				return decodeError(packet);
			case (EOF_BYTE) :
				return decodeEof(packet);
			default :
				throw new UnsupportedOperationException("Unsupported response from server - buggy");
		}
	}

	public static EofResponseMessage decodeEof(PacketBufferReader packet) {
		packet.readByte();
		return new EofResponseMessage(packet.readUnsignedShort(), packet.readUnsignedShort());
	}

	public static OkResponseMessage decodeOk(PacketBufferReader packet) {
		long affectedRows = packet.readVariableLong();
		long insertedId = packet.readVariableLong();
		int serverStatus = packet.readUnsignedShort();
		int warningCount = packet.readUnsignedShort();
		String message = new String(packet.readBytes());
		return new OkResponseMessage(affectedRows, insertedId, serverStatus, warningCount, message);
	}

	public static ErrorResponseMessage decodeError(PacketBufferReader packet) {
		byte[] bytes = packet.readBytes();
		String errorMessage = new String(bytes);
		return new ErrorResponseMessage(errorMessage);
	}

}
