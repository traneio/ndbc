package io.trane.ndbc.mysql.proto.unmarshaller;

import java.nio.charset.Charset;

import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.PacketBufferReader;

public class FieldUnmarshaller extends MysqlUnmarshaller<Field> {

	private final Charset charset;

	public FieldUnmarshaller(Charset charset) {
		this.charset = charset;
	}

	@Override
	public Field decode(final int header, final PacketBufferReader p) {
		final byte[] bytesCatalog = p.readLengthCodedBytes();
		final byte[] bytesDb = p.readLengthCodedBytes();
		final byte[] bytesTable = p.readLengthCodedBytes();
		final byte[] bytesOrigTable = p.readLengthCodedBytes();
		final byte[] bytesName = p.readLengthCodedBytes();
		final byte[] bytesOrigName = p.readLengthCodedBytes();
		p.readVariableLong(); // length of the following fields (always 0x0c)
		final int columnCharset = p.readUnsignedShort();
		final String catalog = new String(bytesCatalog, charset);
		final String db = new String(bytesDb, charset);
		final String table = new String(bytesTable, charset);
		final String origTable = new String(bytesOrigTable, charset);
		final String name = new String(bytesName, charset);
		final String origName = new String(bytesOrigName, charset);
		final long length = p.readUnsignedInt();
		final int fieldType = p.readByte() & 0xFF;
		final int flags = p.readUnsignedShort();
		final int decimals = p.readByte() & 0xFF;
		return new Field(catalog, db, table, origTable, name, origName, columnCharset, length, fieldType, flags,
				decimals);
	}
}
