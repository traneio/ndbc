package io.trane.ndbc.mysql.proto.unmarshaller;

import java.math.BigInteger;
import java.util.List;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.Row;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.value.NullValue;
import io.trane.ndbc.value.Value;

public class BinaryRowUnmarshaller extends MysqlUnmarshaller<Row> {

	private final List<Field> fields;
	private final EncodingRegistry encoding;

	public BinaryRowUnmarshaller(List<Field> fields, EncodingRegistry encoding) {
		this.fields = fields;
		this.encoding = encoding;
	}

	@Override
	protected boolean acceptsHeader(int header) {
		return header == TerminatorUnmarshaller.OK_BYTE;
	}

	@Override
	public Row decode(int header, PacketBufferReader p) {
		p.readByte(); // header
		BigInteger nullBitmap = nullBitmap(p);
		final Value<?>[] values = new Value<?>[fields.size()];
		int i = 0;
		for (Field field : fields) {
			if (nullBitmap.testBit(i + 2))
				values[i] = NullValue.NULL;
			else
				values[i] = encoding.decodeBinary(field.fieldType, p);
			i++;
		}
		return new Row(values);
	}

	private final BigInteger nullBitmap(PacketBufferReader p) {
		byte[] nullBitmap = p.readBytes((fields.size() + 7 + 2) / 8);
		int length = nullBitmap.length;
		byte[] reversed = new byte[length];
		for (int i = 0; i < length; i++)
			reversed[length - 1 - i] = nullBitmap[i];
		return new BigInteger(reversed);
	}
}
