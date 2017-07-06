package io.trane.ndbc.postgres.encoding;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferWriter;

public class TestBufferWriter implements BufferWriter {

  private final ByteBuffer buf;

  public TestBufferWriter(final ByteBuffer buf) {
    super();
    this.buf = buf;
  }

  @Override
  public void writeInt(final int i) {
    buf.putInt(i);
  }

  @Override
  public void writeByte(final byte b) {
    buf.put(b);
  }

  @Override
  public void writeChar(final char b) {
    buf.putChar(b);
  }

  @Override
  public void writeShort(final short s) {
    buf.putShort(s);
  }

  @Override
  public void writeCString(final String s) {
    buf.put(s.getBytes());
    buf.put((byte) 0);
  }

  @Override
  public void writeString(final String s) {
    buf.put(s.getBytes(Charset.forName("UTF-8")));
  }

  @Override
  public void writeBytes(final byte[] b) {
    buf.put(b);
  }

  @Override
  public void writeInts(final int[] i) {
    for (final int v : i)
      buf.putInt(v);
  }

  @Override
  public void writeShorts(final short[] s) {
    for (final short v : s)
      buf.putShort(v);
  }

  @Override
  public void writeLength(final int position) {
    final int previousPosition = buf.position();
    final int length = previousPosition - position;
    buf.position(position);
    buf.putInt(length);
    buf.position(previousPosition);
  }

  @Override
  public void writeLengthNoSelf(final int position) {
    final int previousPosition = buf.position();
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
  public void writeLong(final Long value) {
    buf.putLong(value);
  }

  @Override
  public void writeFloat(final Float value) {
    buf.putFloat(value);
  }

  @Override
  public void writeDouble(final Double value) {
    buf.putDouble(value);
  }
}