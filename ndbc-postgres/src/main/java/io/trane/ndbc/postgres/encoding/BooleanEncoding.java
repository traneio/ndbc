package io.trane.ndbc.postgres.encoding;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.BooleanValue;

public class BooleanEncoding implements Encoding<BooleanValue> {
  
  private static final BooleanValue TRUE = new BooleanValue(true);
  private static final BooleanValue FALSE = new BooleanValue(false);

	@Override
	public String encodeText(BooleanValue value) {
		return value.get() ? "t" : "false";
	}

	@Override
	public BooleanValue decodeText(String value) {
		return (value == "t" || value == "true") ? TRUE : FALSE;
	}

	@Override
	public void encodeBinary(BooleanValue value, BufferWriter b) {
		b.writeByte((byte) (value.get() ? 1 : 0));
	}

	@Override
	public BooleanValue decodeBinary(BufferReader b) {
		return (b.readByte() != 0) ? TRUE : FALSE;
	}

}
