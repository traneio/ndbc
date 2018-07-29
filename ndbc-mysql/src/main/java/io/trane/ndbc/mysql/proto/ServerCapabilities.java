package io.trane.ndbc.mysql.proto;

public class ServerCapabilities {

	public static int SERVER_CLIENT_MYSQL = 1;
	public static int SERVER_FOUND_ROWS = 2;
	public static int SERVER_CONNECT_WITH_DB = 8;
	public static int SERVER_COMPRESS = 32;
	public static int SERVER_LOCAL_FILES = 128;
	public static int SERVER_IGNORE_SPACE = 256;

	public static int SERVER_SSL = 1 << 11;
	public static int SERVER_TRANSACTIONS = 1 << 12;
	public static int SERVER_SECURE_CONNECTION = 1 << 13;
	public static int SERVER_MULTI_STATEMENTS = 1 << 16;
	public static int SERVER_MULTI_RESULTS = 1 << 17;
	public static int SERVER_PS_MULTI_RESULTS = 1 << 18;
	public static int SERVER_PLUGIN_AUTH = 1 << 19;
	public static int CONNECT_ATTRS = 1 << 20;
	public static int PLUGIN_AUTH_LENENC_CLIENT_DATA = 1 << 21;
	public static int CLIENT_SESSION_TRACK = 1 << 23;
	public static int CLIENT_DEPRECATE_EOF = 1 << 24;
}
