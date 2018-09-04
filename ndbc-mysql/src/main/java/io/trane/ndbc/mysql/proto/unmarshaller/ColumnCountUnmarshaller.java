package io.trane.ndbc.mysql.proto.unmarshaller;

import io.trane.ndbc.mysql.proto.Message.ColumnCount;
import io.trane.ndbc.mysql.proto.PacketBufferReader;

public class ColumnCountUnmarshaller extends MysqlUnmarshaller<ColumnCount> {

	@Override
	public ColumnCount decode(final int header, final PacketBufferReader p) {
		return new ColumnCount(p.readVariableLong());
	}
}
