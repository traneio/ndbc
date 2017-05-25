package io.trane.ndbc.proto;

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
}
