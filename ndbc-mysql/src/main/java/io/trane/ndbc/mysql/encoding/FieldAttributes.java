package io.trane.ndbc.mysql.encoding;

public class FieldAttributes {
  public static final short NotNullBitMask     = 1;
  public static final short PrimaryKeyBitMask  = 2;
  public static final short UniqueKeyBitMask   = 4;
  public static final short MultipleKeyBitMask = 8;
  public static final short BlobBitMask        = 16;
  public static final short UnsignedBitMask    = 32;
  public static final short ZeroFillBitMask    = 64;
  public static final short BinaryBitMask      = 128;
}
