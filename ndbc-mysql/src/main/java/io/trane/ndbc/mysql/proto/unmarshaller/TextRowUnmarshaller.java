package io.trane.ndbc.mysql.proto.unmarshaller;

import java.util.List;

import io.trane.ndbc.mysql.encoding.EncodingRegistry;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.Row;
import io.trane.ndbc.mysql.proto.PacketBufferReader;
import io.trane.ndbc.value.Value;

public class TextRowUnmarshaller extends MysqlUnmarshaller<Row> {

	private final List<Field> fields;
	private final EncodingRegistry encoding;

	public TextRowUnmarshaller(List<Field> fields, EncodingRegistry encoding) {
		this.fields = fields;
		this.encoding = encoding;
	}

	@Override
	protected boolean acceptsHeader(int header) {
		return !TerminatorUnmarshaller.isTerminator(header);
	}

	@Override
	public Row decode(int header, PacketBufferReader p) {
		final Value<?>[] values = new Value<?>[fields.size()];
		int i = 0;
		for (Field field : fields) {
			values[i] = encoding.decodeText(field.fieldType, p);
			i++;
		}
		return new Row(values);
	}
}
