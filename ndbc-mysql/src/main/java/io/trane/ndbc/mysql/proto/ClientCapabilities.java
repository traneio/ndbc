package io.trane.ndbc.mysql.proto;

public class ClientCapabilities {
  public static final long CLIENT_PROTOCOL_41       = 0x200;      // 0x00000200
  public static final long CLIENT_TRANSACTIONS      = 0x2000;     // 0x00002000
  public static final long CLIENT_MULTI_RESULTS     = 0x20000;    // 0x00020000
  public static final long CLIENT_PLUGIN_AUTH       = 0x80000;    // 0x00080000
  public static final long CLIENT_SECURE_CONNECTION = 0x8000;     // 0x00008000
  public static final long CLIENT_CONNECT_WITH_DB   = 0x8;        // 0x00000008
  public static final long CLIENT_DEPRECATE_EOF     = 0x01000000; // 0x01000000
}
