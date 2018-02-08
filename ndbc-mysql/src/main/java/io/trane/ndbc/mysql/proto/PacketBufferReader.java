package io.trane.ndbc.mysql.proto;


import io.trane.ndbc.proto.BufferReader;

public class PacketBufferReader implements BufferReader {
  private BufferReader b;
  private int packetLength;
  private int sequence;

  public PacketBufferReader(BufferReader b) {
    byte[] packetHeader = b.readBytes(4);
    this.packetLength = (packetHeader[0] & 0xff) + ((packetHeader[1] & 0xff) << 8) + ((packetHeader[2] & 0xff) << 16);
    this.sequence = packetHeader[3];
    this.b = b.readSlice(this.packetLength);
  }

  public int getSequence() {
    return sequence;
  }

  public int readableBytes() {
    return b.readableBytes();
  }

  public int readInt() {
    return b.readInt();
  }

  public long readVariableLong() {
    int len = Byte.toUnsignedInt(readByte());
    if(len < 251) {
      return len;
    } else if(len == 251) {
      return -1;
    } else if(len == 252) {
      return readUnsignedShort();
    } else if(len == 253) {
      return readUnsignedMiddle();
    } else if(len == 254) {
      return readLong();
    } else {
      throw new IllegalStateException("Invalid length byte: " + len);
    }
  }

  public byte[] readNullTerminatedBytes() {
    final byte[] buf = new byte[100];
    int length = 0;
    for (byte i = b.readByte(); i != 0; i = b.readByte()) {
      buf[length] = i;
      length++;
    }
    final byte[] result = new byte[length];
    System.arraycopy(buf, 0, result, 0, length);
    return result;
  }

  private int readUnsignedMiddle() {
      byte[] bytes = readBytes(3);
      int value =
              ((bytes[0] & 0xFF) <<  0) |
              ((bytes[1] & 0xFF) <<  8) |
              ((bytes[2] & 0xFF) << 16);
      return value;
  }

  public long readUnsignedInt() {
    byte[] bytes = readBytes(4);
    long value =
            ((bytes[0] & 0xFF) <<  0) |
            ((bytes[1] & 0xFF) <<  8) |
            ((bytes[2] & 0xFF) << 16) |
            ((bytes[3] & 0xFF) << 24);
      return value;
  }

  public byte[] readLengthCodedBytes() {
    byte lengthByte = readByte() ;
    int length = lengthByte & 0xFF;
    return readBytes(length);
  }

  public byte readByte() {
    return b.readByte();
  }

  public short readShort() {
    return b.readShort();
  }

  public int readUnsignedShort() {
    byte[] bytes = readBytes(2);
    int value =
            ((bytes[0] & 0xFF) <<  0) |
            ((bytes[1] & 0xFF) <<  8) ;
    return value;
  }

  public String readCString() {
    return b.readCString();
  }

  public String readCString(int length) {
    return b.readCString(length);
  }

  public String readString() {
    return b.readString();
  }

  public String readString(int length) {
    return b.readString(length);
  }

  public byte[] readBytes() {
    return b.readBytes();
  }

  public byte[] readBytes(int length) {
    return b.readBytes(length);
  }

  public int[] readInts() {
    return b.readInts();
  }

  public int[] readInts(int length) {
    return b.readInts(length);
  }

  public short[] readShorts() {
    return b.readShorts();
  }

  public short[] readShorts(int length) {
    return b.readShorts(length);
  }

  public BufferReader readSlice(int length) {
    return b.readSlice(length);
  }

  public void markReaderIndex() {
    b.markReaderIndex();
  }

  public void resetReaderIndex() {
    b.resetReaderIndex();
  }

  public void retain() {
    b.retain();
  }

  public void release() {
    b.release();
  }

  public Long readLong() {
    return b.readLong();
  }

  public Float readFloat() {
    return b.readFloat();
  }

  public Double readDouble() {
    return b.readDouble();
  }
}
