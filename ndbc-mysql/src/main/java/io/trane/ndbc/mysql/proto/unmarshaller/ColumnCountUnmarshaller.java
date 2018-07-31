package io.trane.ndbc.mysql.proto.unmarshaller;

import java.util.LinkedList;
import java.util.List;

import io.trane.ndbc.mysql.proto.Message.ColumnCount;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.TextResultSet;
import io.trane.ndbc.mysql.proto.Message.TextRow;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.proto.BufferReader;

public class ColumnCountUnmarshaller {

	public ColumnCount decode(final BufferReader br) {
		final PacketBufferReader packet = new PacketBufferReader(br);
		// int returnCode = packet.readByte();
		final long numCols = packet.readVariableLong();
		final List<Field> fields = new LinkedList<>();
		for (int i = 0; i < numCols; i++) {
			fields.add(decodeField(new PacketBufferReader(br)));
		}

		ServerResponseUnmarshaller.decodeEof(new PacketBufferReader(br));

		final List<TextRow> rows = new LinkedList<>();
		boolean eof = false;
		while (!eof) {
			final PacketBufferReader rowPacket = new PacketBufferReader(br);
			rowPacket.markReaderIndex();
			if ((rowPacket.readByte() & 0xFF) == ServerResponseUnmarshaller.EOF_BYTE) {
				rowPacket.resetReaderIndex();
				ServerResponseUnmarshaller.decodeEof(rowPacket);
				eof = true;
			} else {
				rowPacket.resetReaderIndex();
				final List<String> values = new LinkedList<>();
				for (int i = 0; i < numCols; i++) {
					values.add(new String(rowPacket.readLengthCodedBytes())); // TODO user correct charset
				}
				rows.add(new TextRow(values));
			}
		}
		return null;
	}

	public Field decodeField(final PacketBufferReader br) {
		final byte[] bytesCatalog = br.readLengthCodedBytes();
		final byte[] bytesDb = br.readLengthCodedBytes();
		final byte[] bytesTable = br.readLengthCodedBytes();
		final byte[] bytesOrigTable = br.readLengthCodedBytes();
		final byte[] bytesName = br.readLengthCodedBytes();
		final byte[] bytesOrigName = br.readLengthCodedBytes();
		br.readVariableLong(); // length of the following fields (always 0x0c)
		final int charset = br.readUnsignedShort();
		final String catalog = new String(bytesCatalog); // TODO consider charset
		final String db = new String(bytesDb);
		final String table = new String(bytesTable);
		final String origTable = new String(bytesOrigTable);
		final String name = new String(bytesName);
		final String origName = new String(bytesOrigName);
		final long length = br.readUnsignedInt();
		final int fieldType = br.readByte() & 0xFF;
		final int flags = br.readUnsignedShort();
		final int decimals = br.readByte() & 0xFF;
		return new Field(catalog, db, table, origTable, name, origName, charset, length, fieldType, flags, decimals);
	}
}
