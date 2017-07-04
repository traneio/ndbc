package io.trane.ndbc.postgres.encoding;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import io.trane.ndbc.proto.BufferReader;

public class TestBufferReader implements BufferReader {

  private final ByteBuffer buf;

  public TestBufferReader(ByteBuffer buf) {
    super();
    this.buf = buf;
  }

  @Override
  public int readableBytes() {
    return buf.remaining();
  }

  @Override
  public int readInt() {
    return buf.getInt();
  }

  @Override
  public byte readByte() {
    return buf.get();
  }

  @Override
  public short readShort() {
    return buf.getShort();
  }

  @Override
  public String readCString() {
    final int stringSize = bytesBefore((byte) 0);
    final String string = readString(stringSize);
    buf.position(buf.position() + 1); // skip 0
    return string;
  }

  private int bytesBefore(byte b) {
    int pos = buf.position();
    while (buf.remaining() != 0 && buf.get() != b) {
    }
    int found = buf.position() - 1;
    buf.position(pos);
    return found;
  }

  @Override
  public String readCString(int length) {
    final String string = readString(length);
    buf.position(buf.position() + 1); // skip 0
    return string;
  }

  @Override
  public String readString() {
    return readString(buf.remaining());
  }

  @Override
  public String readString(int length) {
    final byte[] b = new byte[length];
    buf.get(b);
    final String string = new String(b, Charset.forName("UTF-8"));
    return string;
  }

  @Override
  public byte[] readBytes() {
    return readBytes(buf.remaining());
  }

  @Override
  public byte[] readBytes(int length) {
    final byte[] bytes = new byte[length];
    buf.get(bytes);
    return bytes;
  }

  @Override
  public int[] readInts() {
    return readInts(buf.remaining() / 4);
  }

  @Override
  public int[] readInts(int length) {
    final int[] ints = new int[length];
    for (int i = 0; i < length; i++)
      ints[i] = buf.getInt();
    return ints;
  }

  @Override
  public short[] readShorts() {
    return readShorts(buf.remaining() / 2);
  }

  @Override
  public short[] readShorts(int length) {
    final short[] shorts = new short[length];
    for (int i = 0; i < length; i++)
      shorts[i] = buf.getShort();
    return shorts;
  }

  @Override
  public BufferReader readSlice(int length) {
    byte[] slice = new byte[length];
    buf.get(slice);
    return new TestBufferReader(ByteBuffer.wrap(slice));
  }

  @Override
  public void markReaderIndex() {
    buf.mark();
  }

  @Override
  public void resetReaderIndex() {
    buf.reset();
  }

  @Override
  public void retain() {
  }

  @Override
  public void release() {
  }

  @Override
  public Long readLong() {
    return buf.getLong();
  }

  @Override
  public Float readFloat() {
    return buf.getFloat();
  }

  @Override
  public Double readDouble() {
    return buf.getDouble();
  }

}