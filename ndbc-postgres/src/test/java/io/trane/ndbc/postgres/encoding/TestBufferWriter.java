package io.trane.ndbc.postgres.encoding;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferWriter;

public class TestBufferWriter implements BufferWriter {

  private final ByteBuffer buf;

  public TestBufferWriter(ByteBuffer buf) {
    super();
    this.buf = buf;
  }

  @Override
  public void writeInt(int i) {
    buf.putInt(i);
  }

  @Override
  public void writeByte(byte b) {
    buf.put(b);
  }

  @Override
  public void writeChar(char b) {
    buf.putChar(b);
  }

  @Override
  public void writeShort(short s) {
    buf.putShort(s);
  }

  @Override
  public void writeCString(String s) {
    buf.put(s.getBytes());
    buf.put((byte) 0);
  }

  @Override
  public void writeString(String s) {
    buf.put(s.getBytes(Charset.forName("UTF-8")));
  }

  @Override
  public void writeBytes(byte[] b) {
    buf.put(b);
  }

  @Override
  public void writeInts(int[] i) {
    for (int v : i)
      buf.putInt(v);
  }

  @Override
  public void writeShorts(short[] s) {
    for (short v : s)
      buf.putShort(v);
  }

  @Override
  public void writeLength(int position) {
    int previousPosition = buf.position();
    final int length = previousPosition - position;
    buf.position(position);
    buf.putInt(length);
    buf.position(previousPosition);
  }

  @Override
  public void writeLengthNoSelf(int position) {
    int previousPosition = buf.position();
    final int length = previousPosition - position - 4;
    buf.position(position);
    buf.putInt(length);
    buf.position(previousPosition);
  }

  @Override
  public int writerIndex() {
    return buf.position();
  }

  @Override
  public void writeLong(Long value) {
    buf.putLong(value);
  }

  @Override
  public void writeFloat(Float value) {
    buf.putFloat(value);
  }

  @Override
  public void writeDouble(Double value) {
    buf.putDouble(value);
  }
}