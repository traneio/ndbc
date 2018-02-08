package io.trane.ndbc.mysql.proto;

public class ClientCapabilities {
  public static long CLIENT_PROTOCOL_41 = 0x200;
  public static long CLIENT_TRANSACTIONS = 0x2000;
  public static long CLIENT_MULTI_RESULTS = 0x20000;
  public static long CLIENT_PLUGIN_AUTH = 0x80000;
  public static long CLIENT_SECURE_CONNECTION = 0x8000;
  public static long CLIENT_CONNECT_WITH_DB = 0x8;
}
