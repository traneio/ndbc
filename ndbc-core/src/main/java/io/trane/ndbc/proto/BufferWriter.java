package io.trane.ndbc.proto;

import java.nio.charset.Charset;

public interface BufferWriter {

	void writeInt(int i);

	void writeByte(byte b);

	void writeChar(char b);

	void writeShort(short s);

	void writeCString(String s);

	void writeString(String s);

	void writeBytes(byte[] b);

	void writeInts(int[] i);

	void writeShorts(short[] s);

	void writeLength(int position);

	void writeLengthNoSelf(int position);

	int writerIndex();

	void writeLong(Long value);

	void writeFloat(Float value);

	void writeDouble(Double value);

	Charset getCharset();
}
