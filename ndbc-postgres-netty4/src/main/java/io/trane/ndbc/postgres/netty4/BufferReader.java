package io.trane.ndbc.postgres.netty4;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public class BufferReader implements io.trane.ndbc.proto.BufferReader {

  private final Charset charset;
  private final ByteBuf bb;

  public BufferReader(Charset charset, ByteBuf bb) {
    super();
    this.charset = charset;
    this.bb = bb;
  }

  @Override
  public int readInt() {
    return bb.readInt();
  }

  @Override
  public short readShort() {
    return bb.readShort();
  }

  @Override
  public String readCString() {
    int stringSize = bb.bytesBefore((byte) 0);
    String string = readString(stringSize);
    bb.skipBytes(1); // skip 0
    return string;
  }
  
  @Override
  public String readCString(int length) {
    String string = readString(length);
    bb.skipBytes(1); // 0
    return string;
  }

  @Override
  public String readString() {
    return readString(bb.readableBytes());
  }

  @Override
  public String readString(int length) {
    byte[] b = new byte[length];
    bb.readBytes(b);
    String string = new String(b, charset);
    return string;
  }

  @Override
  public int readableBytes() {
    return bb.readableBytes();
  }

  @Override
  public byte readByte() {
    return bb.readByte();
  }

  @Override
  public byte[] readBytes() {
    return readBytes(bb.readableBytes());
  }

  @Override
  public byte[] readBytes(int length) {
    byte[] bytes = new byte[length];
    bb.readBytes(bytes);
    return bytes;
  }

  @Override
  public BufferReader readSlice(int length) {
    return new BufferReader(charset, bb.readSlice(length));
  }

  @Override
  public int[] readInts() {
    return readInts(bb.readableBytes() / 4);
  }

  @Override
  public int[] readInts(int length) {
    int[] ints = new int[length];
    for (int i = 0; i < length; i++)
      ints[i] = bb.readInt();
    return ints;
  }

  @Override
  public short[] readShorts() {
    return readShorts(bb.readableBytes() / 2);
  }

  @Override
  public short[] readShorts(int length) {
    short[] shorts = new short[length];
    for (int i = 0; i < length; i++)
      shorts[i] = bb.readShort();
    return shorts;
  }
  
  @Override
  public Long readLong() {
    return bb.readLong();
  }
  
  @Override
  public Float readFloat() {
    return bb.readFloat();
  }
  
  @Override
  public Double readDouble() {
    return bb.readDouble();
  }
  
  @Override
  public void retain() {
    bb.retain();
  }
  
  @Override
  public void release() {
    bb.release();
  }

  @Override
  public void markReaderIndex() {
    bb.markReaderIndex();
  }

  @Override
  public void resetReaderIndex() {
    bb.resetReaderIndex();
  }
}
