package io.trane.ndbc.sqlserver.proto;

public interface TDS {

  // TDS protocol versions
  int VER_DENALI = 0x74000004; // TDS 7.4
  int VER_KATMAI = 0x730B0003; // TDS 7.3B(includes null bit compression)
  int VER_YUKON = 0x72090002; // TDS 7.2
  int VER_UNKNOWN = 0x00000000; // Unknown/uninitialized

  int TDS_RET_STAT = 0x79;
  int TDS_COLMETADATA = 0x81;
  int TDS_TABNAME = 0xA4;
  int TDS_COLINFO = 0xA5;
  int TDS_ORDER = 0xA9;
  int TDS_ERR = 0xAA;
  int TDS_MSG = 0xAB;
  int TDS_RETURN_VALUE = 0xAC;
  int TDS_LOGIN_ACK = 0xAD;
  int TDS_FEATURE_EXTENSION_ACK = 0xAE;
  int TDS_ROW = 0xD1;
  int TDS_NBCROW = 0xD2;
  int TDS_ENV_CHG = 0xE3;
  int TDS_SSPI = 0xED;
  int TDS_DONE = 0xFD;
  int TDS_DONEPROC = 0xFE;
  int TDS_DONEINPROC = 0xFF;
  int TDS_FEDAUTHINFO = 0xEE;
  int TDS_SQLRESCOLSRCS = 0xa2;
  int TDS_SQLDATACLASSIFICATION = 0xa3;

  // FedAuth
  byte TDS_FEATURE_EXT_FEDAUTH = 0x02;
  int TDS_FEDAUTH_LIBRARY_SECURITYTOKEN = 0x01;
  int TDS_FEDAUTH_LIBRARY_ADAL = 0x02;
  int TDS_FEDAUTH_LIBRARY_RESERVED = 0x7F;
  byte ADALWORKFLOW_ACTIVEDIRECTORYPASSWORD = 0x01;
  byte ADALWORKFLOW_ACTIVEDIRECTORYINTEGRATED = 0x02;
  byte FEDAUTH_INFO_ID_STSURL = 0x01; // FedAuthInfoData is token endpoint URL from which to acquire fed
                                      // auth token
  byte FEDAUTH_INFO_ID_SPN = 0x02; // FedAuthInfoData is the SPN to use for acquiring fed auth token

  // AE constants
  // 0x03 is for x_eFeatureExtensionId_Rcs
  byte TDS_FEATURE_EXT_AE = 0x04;
  byte MAX_SUPPORTED_TCE_VERSION = 0x01; // max version
  int CUSTOM_CIPHER_ALGORITHM_ID = 0; // max version
  // 0x06 is for x_eFeatureExtensionId_LoginToken
  // 0x07 is for x_eFeatureExtensionId_ClientSideTelemetry
  // Data Classification constants
  byte TDS_FEATURE_EXT_DATACLASSIFICATION = 0x09;
  byte DATA_CLASSIFICATION_NOT_ENABLED = 0x00;
  byte MAX_SUPPORTED_DATA_CLASSIFICATION_VERSION = 0x01;

  int AES_256_CBC = 1;
  int AEAD_AES_256_CBC_HMAC_SHA256 = 2;
  int AE_METADATA = 0x08;

  byte TDS_FEATURE_EXT_UTF8SUPPORT = 0x0A;

  int TDS_TVP = 0xF3;
  int TVP_ROW = 0x01;
  int TVP_NULL_TOKEN = 0xFFFF;
  int TVP_STATUS_DEFAULT = 0x02;

  int TVP_ORDER_UNIQUE_TOKEN = 0x10;
  // TVP_ORDER_UNIQUE_TOKEN flags
  byte TVP_ORDERASC_FLAG = 0x1;
  byte TVP_ORDERDESC_FLAG = 0x2;
  byte TVP_UNIQUE_FLAG = 0x4;

  // TVP flags, may be used in other places
  int FLAG_NULLABLE = 0x01;
  int FLAG_TVP_DEFAULT_COLUMN = 0x200;

  int FEATURE_EXT_TERMINATOR = -1;

  // Sql_variant length
  int SQL_VARIANT_LENGTH = 8009;

  // RPC ProcIDs for use with RPCRequest (PKT_RPC) calls
  short PROCID_SP_CURSOR = 1;
  short PROCID_SP_CURSOROPEN = 2;
  short PROCID_SP_CURSORPREPARE = 3;
  short PROCID_SP_CURSOREXECUTE = 4;
  short PROCID_SP_CURSORPREPEXEC = 5;
  short PROCID_SP_CURSORUNPREPARE = 6;
  short PROCID_SP_CURSORFETCH = 7;
  short PROCID_SP_CURSOROPTION = 8;
  short PROCID_SP_CURSORCLOSE = 9;
  short PROCID_SP_EXECUTESQL = 10;
  short PROCID_SP_PREPARE = 11;
  short PROCID_SP_EXECUTE = 12;
  short PROCID_SP_PREPEXEC = 13;
  short PROCID_SP_PREPEXECRPC = 14;
  short PROCID_SP_UNPREPARE = 15;

  // Constants for use with cursor RPCs
  short SP_CURSOR_OP_UPDATE = 1;
  short SP_CURSOR_OP_DELETE = 2;
  short SP_CURSOR_OP_INSERT = 4;
  short SP_CURSOR_OP_REFRESH = 8;
  short SP_CURSOR_OP_LOCK = 16;
  short SP_CURSOR_OP_SETPOSITION = 32;
  short SP_CURSOR_OP_ABSOLUTE = 64;

  // Constants for server-cursored result sets.
  // See the Engine Cursors Functional Specification for details.
  int FETCH_FIRST = 1;
  int FETCH_NEXT = 2;
  int FETCH_PREV = 4;
  int FETCH_LAST = 8;
  int FETCH_ABSOLUTE = 16;
  int FETCH_RELATIVE = 32;
  int FETCH_REFRESH = 128;
  int FETCH_INFO = 256;
  int FETCH_PREV_NOADJUST = 512;
  byte RPC_OPTION_NO_METADATA = (byte) 0x02;

  // Transaction manager request types
  short TM_GET_DTC_ADDRESS = 0;
  short TM_PROPAGATE_XACT = 1;
  short TM_BEGIN_XACT = 5;
  short TM_PROMOTE_PROMOTABLE_XACT = 6;
  short TM_COMMIT_XACT = 7;
  short TM_ROLLBACK_XACT = 8;
  short TM_SAVE_XACT = 9;

  byte PKT_QUERY = 1;
  byte PKT_RPC = 3;
  byte PKT_REPLY = 4;
  byte PKT_CANCEL_REQ = 6;
  byte PKT_BULK = 7;
  byte PKT_DTC = 14;
  byte PKT_LOGON70 = 16; // 0x10
  byte PKT_SSPI = 17;
  byte PKT_PRELOGIN = 18; // 0x12
  byte PKT_FEDAUTH_TOKEN_MESSAGE = 8; // Authentication token for federated authentication

  byte STATUS_NORMAL = 0x00;
  byte STATUS_BIT_EOM = 0x01;
  byte STATUS_BIT_ATTENTION = 0x02;// this is called ignore bit in TDS spec
  byte STATUS_BIT_RESET_CONN = 0x08;

  // Various TDS packet size constants
  int INVALID_PACKET_SIZE = -1;
  int INITIAL_PACKET_SIZE = 4096;
  int MIN_PACKET_SIZE = 512;
  int MAX_PACKET_SIZE = 32767;
  int DEFAULT_PACKET_SIZE = 8000;
  int SERVER_PACKET_SIZE = 0; // Accept server's configured packet size

  // TDS packet header size and offsets
  int PACKET_HEADER_SIZE = 8;
  int PACKET_HEADER_MESSAGE_TYPE = 0;
  int PACKET_HEADER_MESSAGE_STATUS = 1;
  int PACKET_HEADER_MESSAGE_LENGTH = 2;
  int PACKET_HEADER_SPID = 4;
  int PACKET_HEADER_SEQUENCE_NUM = 6;
  int PACKET_HEADER_WINDOW = 7; // Reserved/Not used

  // MARS header length:
  // 2 byte header type
  // 8 byte transaction descriptor
  // 4 byte outstanding request count
  int MARS_HEADER_LENGTH = 18; // 2 byte header type, 8 byte transaction descriptor,
  int TRACE_HEADER_LENGTH = 26; // header length (4) + header type (2) + guid (16) + Sequence number size
                                // (4)

  short HEADERTYPE_TRACE = 3; // trace header type

  // Message header length
  int MESSAGE_HEADER_LENGTH = MARS_HEADER_LENGTH + 4; // length includes message header itself

  byte B_PRELOGIN_OPTION_VERSION = 0x00;
  byte B_PRELOGIN_OPTION_ENCRYPTION = 0x01;
  byte B_PRELOGIN_OPTION_INSTOPT = 0x02;
  byte B_PRELOGIN_OPTION_THREADID = 0x03;
  byte B_PRELOGIN_OPTION_MARS = 0x04;
  byte B_PRELOGIN_OPTION_TRACEID = 0x05;
  byte B_PRELOGIN_OPTION_FEDAUTHREQUIRED = 0x06;
  byte B_PRELOGIN_OPTION_TERMINATOR = (byte) 0xFF;

  // Login option byte 1
  byte LOGIN_OPTION1_ORDER_X86 = 0x00;
  byte LOGIN_OPTION1_ORDER_6800 = 0x01;
  byte LOGIN_OPTION1_CHARSET_ASCII = 0x00;
  byte LOGIN_OPTION1_CHARSET_EBCDIC = 0x02;
  byte LOGIN_OPTION1_FLOAT_IEEE_754 = 0x00;
  byte LOGIN_OPTION1_FLOAT_VAX = 0x04;
  byte LOGIN_OPTION1_FLOAT_ND5000 = 0x08;
  byte LOGIN_OPTION1_DUMPLOAD_ON = 0x00;
  byte LOGIN_OPTION1_DUMPLOAD_OFF = 0x10;
  byte LOGIN_OPTION1_USE_DB_ON = 0x00;
  byte LOGIN_OPTION1_USE_DB_OFF = 0x20;
  byte LOGIN_OPTION1_INIT_DB_WARN = 0x00;
  byte LOGIN_OPTION1_INIT_DB_FATAL = 0x40;
  byte LOGIN_OPTION1_SET_LANG_OFF = 0x00;
  byte LOGIN_OPTION1_SET_LANG_ON = (byte) 0x80;

  // Login option byte 2
  byte LOGIN_OPTION2_INIT_LANG_WARN = 0x00;
  byte LOGIN_OPTION2_INIT_LANG_FATAL = 0x01;
  byte LOGIN_OPTION2_ODBC_OFF = 0x00;
  byte LOGIN_OPTION2_ODBC_ON = 0x02;
  byte LOGIN_OPTION2_TRAN_BOUNDARY_OFF = 0x00;
  byte LOGIN_OPTION2_TRAN_BOUNDARY_ON = 0x04;
  byte LOGIN_OPTION2_CACHE_CONNECTION_OFF = 0x00;
  byte LOGIN_OPTION2_CACHE_CONNECTION_ON = 0x08;
  byte LOGIN_OPTION2_USER_NORMAL = 0x00;
  byte LOGIN_OPTION2_USER_SERVER = 0x10;
  byte LOGIN_OPTION2_USER_REMUSER = 0x20;
  byte LOGIN_OPTION2_USER_SQLREPL = 0x30;
  byte LOGIN_OPTION2_INTEGRATED_SECURITY_OFF = 0x00;
  byte LOGIN_OPTION2_INTEGRATED_SECURITY_ON = (byte) 0x80;

  // Login option byte 3
  byte LOGIN_OPTION3_DEFAULT = 0x00;
  byte LOGIN_OPTION3_CHANGE_PASSWORD = 0x01;
  byte LOGIN_OPTION3_SEND_YUKON_BINARY_XML = 0x02;
  byte LOGIN_OPTION3_USER_INSTANCE = 0x04;
  byte LOGIN_OPTION3_UNKNOWN_COLLATION_HANDLING = 0x08;
  byte LOGIN_OPTION3_FEATURE_EXTENSION = 0x10;

  // Login type flag (bits 5 - 7 reserved for future use)
  byte LOGIN_SQLTYPE_DEFAULT = 0x00;
  byte LOGIN_SQLTYPE_TSQL = 0x01;
  byte LOGIN_SQLTYPE_ANSI_V1 = 0x02;
  byte LOGIN_SQLTYPE_ANSI89_L1 = 0x03;
  byte LOGIN_SQLTYPE_ANSI89_L2 = 0x04;
  byte LOGIN_SQLTYPE_ANSI89_IEF = 0x05;
  byte LOGIN_SQLTYPE_ANSI89_ENTRY = 0x06;
  byte LOGIN_SQLTYPE_ANSI89_TRANS = 0x07;
  byte LOGIN_SQLTYPE_ANSI89_INTER = 0x08;
  byte LOGIN_SQLTYPE_ANSI89_FULL = 0x09;

  byte LOGIN_OLEDB_OFF = 0x00;
  byte LOGIN_OLEDB_ON = 0x10;

  byte LOGIN_READ_ONLY_INTENT = 0x20;
  byte LOGIN_READ_WRITE_INTENT = 0x00;

  byte ENCRYPT_OFF = 0x00;
  byte ENCRYPT_ON = 0x01;
  byte ENCRYPT_NOT_SUP = 0x02;
  byte ENCRYPT_REQ = 0x03;
  byte ENCRYPT_INVALID = (byte) 0xFF;

  // Prelogin packet length, including the tds header,
  // version, encrpytion, and traceid data sessions.
  // For detailed info, please check the definition of
  // preloginRequest in Prelogin function.
  byte B_PRELOGIN_MESSAGE_LENGTH = 67;
  byte B_PRELOGIN_MESSAGE_LENGTH_WITH_FEDAUTH = 73;

  // Scroll options and concurrency options lifted out
  // of the the Yukon cursors spec for sp_cursoropen.
  int SCROLLOPT_KEYSET = 1;
  int SCROLLOPT_DYNAMIC = 2;
  int SCROLLOPT_FORWARD_ONLY = 4;
  int SCROLLOPT_STATIC = 8;
  int SCROLLOPT_FAST_FORWARD = 16;

  int SCROLLOPT_PARAMETERIZED_STMT = 4096;
  int SCROLLOPT_AUTO_FETCH = 8192;
  int SCROLLOPT_AUTO_CLOSE = 16384;

  int CCOPT_READ_ONLY = 1;
  int CCOPT_SCROLL_LOCKS = 2;
  int CCOPT_OPTIMISTIC_CC = 4;
  int CCOPT_OPTIMISTIC_CCVAL = 8;
  int CCOPT_ALLOW_DIRECT = 8192;
  int CCOPT_UPDT_IN_PLACE = 16384;

  // Result set rows include an extra, "hidden" ROWSTAT column which indicates
  // the overall success or failure of the row fetch operation. With a keyset
  // cursor, the value in the ROWSTAT column indicates whether the row has been
  // deleted from the database.
  int ROWSTAT_FETCH_SUCCEEDED = 1;
  int ROWSTAT_FETCH_MISSING = 2;

  // ColumnInfo status
  int COLINFO_STATUS_EXPRESSION = 0x04;
  int COLINFO_STATUS_KEY = 0x08;
  int COLINFO_STATUS_HIDDEN = 0x10;
  int COLINFO_STATUS_DIFFERENT_NAME = 0x20;

  int MAX_FRACTIONAL_SECONDS_SCALE = 7;

  static String getEncryptionLevel(final int level) {
    switch (level) {
    case ENCRYPT_OFF:
      return "OFF";
    case ENCRYPT_ON:
      return "ON";
    case ENCRYPT_NOT_SUP:
      return "NOT SUPPORTED";
    case ENCRYPT_REQ:
      return "REQUIRED";
    default:
      return "unknown encryption level (0x" + Integer.toHexString(level).toUpperCase() + ")";
    }
  }
}
