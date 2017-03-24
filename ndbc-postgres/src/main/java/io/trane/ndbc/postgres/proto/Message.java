package io.trane.ndbc.postgres.proto;

interface Message {

  interface BackendMessage extends Message {
  }

  interface FrontendMessage extends Message {
  }

  /** Identifies the message as an authentication request. */
  interface AuthenticationRequest extends BackendMessage {

    /** Specifies that the authentication was successful. */
    interface AuthenticationOk extends AuthenticationRequest {
    }

    /** Specifies that Kerberos V5 authentication is required. */
    interface AuthenticationKerberosV5 extends AuthenticationRequest {
    }

    /** Specifies that a clear-text password is required. */
    interface AuthenticationCleartextPassword extends AuthenticationRequest {
    }

    /** Specifies that an MD5-encrypted password is required. */
    interface AuthenticationMD5Password extends AuthenticationRequest {
      /** The salt to use when encrypting the password. */
      byte[] salt();
    }

    /** Specifies that an SCM credentials message is required. */
    interface AuthenticationSCMCredential extends AuthenticationRequest {
    }

    /** Specifies that GSSAPI authentication is required. */
    interface AuthenticationGSS extends AuthenticationRequest {
    }

    /** Specifies that SSPI authentication is required. */
    interface AuthenticationSSPI extends AuthenticationRequest {
    }

    /** Specifies that this message contains GSSAPI or SSPI data. */
    interface AuthenticationGSSContinue extends AuthenticationRequest {
      /** GSSAPI or SSPI authentication data. */
      byte[] authenticationData();
    }
  }

  /** Identifies the message as cancellation key data. The frontend 
   * must save these values if it wishes to be able to issue CancelRequest 
   * messages later. */
  interface BackendKeyData extends BackendMessage {
    /** The process ID of this backend. */
    int processId();

    /** The secret key of this backend. */
    int secretKey();
  }

  /** Identifies the message as a Bind command. */
  interface Bind extends FrontendMessage {

    /** The name of the destination portal (an empty string selects 
     * the unnamed portal). */
    String destinationPortalName();

    /** The name of the source prepared statement (an empty string 
     * selects the unnamed prepared statement). */
    String sourcePreparedStatementName();

    /** The number of parameter format codes that follow (denoted C below). 
     * This can be zero to indicate that there are no parameters or that 
     * the parameters all use the default format (text); or one, in which 
     * case the specified format code is applied to all parameters; or it 
     * can equal the actual number of parameters.
     * 
     * The parameter format codes. Each must presently be zero (text) or 
     * one (binary). */
    short[] parameterFormatCodes();

    /** The number of parameter values that follow (possibly zero). This 
     * must match the number of parameters needed by the query.
     * Next, the following pair of fields appear for each parameter: 
     * The value of the parameter, in the format indicated by the associated 
     * format code. n is the above length.*/
    byte[][] fields();

    /** The result-column format codes. Each must presently be zero (text) 
     * or one (binary). */
    short[] resultColumnFormatCodes();
  }

  interface BindComplete extends BackendMessage {
  }

  interface CancelRequest extends FrontendMessage {
    /** The process ID of the target backend. */
    int processId();

    /** The secret key for the target backend. */
    int secretKey();
  }

  /** Identifies the message as a Close command. */
  interface Close extends FrontendMessage {

    /** The name of the prepared statement or portal to close (an empty 
     * string selects the unnamed prepared statement or portal). */
    String name();

    interface ClosePreparedStatement extends Close {
    }

    interface ClosePortal extends Close {
    }
  }

  interface CloseComplete extends BackendMessage {
  }

  /** Identifies the message as a command-completed response. */
  interface CommandComplete extends BackendMessage {

    int rows();

    /** For an INSERT command, the tag is INSERT oid rows, where 
     * rows is the number of rows inserted. oid is the object ID 
     * of the inserted row if rows is 1 and the target table has 
     * OIDs; otherwise oid is 0. */
    interface InsertComplete extends CommandComplete {
      String oid();
    }

    /** For a DELETE command, the tag is DELETE rows where rows is 
     * the number of rows deleted. */
    interface DeleteComplete extends CommandComplete {
    }

    /** For an UPDATE command, the tag is UPDATE rows where rows is 
     * the number of rows updated. */
    interface UpdateComplete extends CommandComplete {
    }

    /** For a SELECT or CREATE TABLE AS command, the tag is SELECT 
     * rows where rows is the number of rows retrieved. */
    interface SelectorOrCreateTableAsCompleted extends CommandComplete {
    }

    /** For a MOVE command, the tag is MOVE rows where rows is the 
     * number of rows the cursor's position has been changed by. */
    interface MoveComplete extends CommandComplete {
    }

    /** For a FETCH command, the tag is FETCH rows where rows is the 
     * number of rows that have been retrieved from the cursor. */
    interface FetchComplete extends CommandComplete {
    }

    /** For a COPY command, the tag is COPY rows where rows is the 
     * number of rows copied. (Note: the row count appears only in 
     * PostgreSQL 8.2 and later.) */
    interface CopyComplete extends CommandComplete {
    }
  }

  /** Identifies the message as COPY data. */
  interface CopyData extends FrontendMessage, BackendMessage {
    /** Data that forms part of a COPY data stream. Messages sent 
     * from the backend will always correspond to single data rows, 
     * but messages sent by frontends might divide the data stream 
     * arbitrarily. */
    byte[] data();
  }

  /** Identifies the message as a COPY-complete indicator. */
  interface CopyDone extends FrontendMessage, BackendMessage {
  }

  /** Identifies the message as a COPY-failure indicator. */
  interface CopyFail extends FrontendMessage {
    /** An error message to report as the cause of failure. */
    String errorMessage();
  }

  /** Identifies the message as a Start Copy In response. The 
   * frontend must now send copy-in data (if not prepared to do 
   * so, send a CopyFail message). */
  interface CopyInResponse extends BackendMessage {

    /** The format codes to be used for each column. Each must 
     * presently be zero (text) or one (binary). All must be zero 
     * if the overall copy format is textual. */
    short[] columnFormatCodes();
  }

  /** Identifies the message as a Start Copy Out response. 
   * This message will be followed by copy-out data. */
  interface CopyOutResponse extends BackendMessage {

    /** The format codes to be used for each column. Each must 
     * presently be zero (text) or one (binary). All must be 
     * zero if the overall copy format is textual. */
    short[] columnFormatCodes();
  }

  /** Identifies the message as a Start Copy Both response. 
   * This message is used only for Streaming Replication. */
  interface CopyBothResponse extends BackendMessage {

    /** The format codes to be used for each column. Each must 
     * presently be zero (text) or one (binary). All must be 
     * zero if the overall copy format is textual. */
    short[] columnFormatCodes();
  }

  /** Identifies the message as a data row. */
  interface DataRow extends BackendMessage {
    /** The value of the column, in the format indicated by the 
     * associated format code. n is the above length. */
    byte[] columns();
  }

  /** Identifies the message as a Describe command. */
  interface Describe extends FrontendMessage {

    /** The name of the prepared statement or portal to describe 
     * (an empty string selects the unnamed prepared statement 
     * or portal). */
    String name();

    interface DescribePreparedStatement extends Describe {
    }

    interface DescribePortal extends Describe {
    }
  }

  /** Identifies the message as a response to an empty query string. 
   * (This substitutes for CommandComplete.) */
  interface EmptyQueryResponse extends BackendMessage {
  }

  /** Identifies the message as an error. */
  interface ErrorResponse extends BackendMessage {
    interface Field {
      /** A code identifying the field type; if zero, this is the message 
       * terminator and no string follows. The presently defined field 
       * types are listed in Section 51.6. Since more field types might 
       * be added in future, frontends should silently ignore fields of 
       * unrecognized type. */
      char type();

      /** The field value. */
      String value();
    }

    /** The message body consists of one or more identified fields, followed 
     * by a zero byte as a terminator. Fields can appear in any order. For 
     * each field there is the following: */
    Field[] fields();
  }

  /** Identifies the message as an Execute command. */
  interface Execute extends FrontendMessage {
    /** The name of the portal to execute (an empty string selects the unnamed 
     * portal). */
    String portalName();

    /** Maximum number of rows to return, if portal contains a query that returns 
     * rows (ignored otherwise). Zero denotes "no limit". */
    int maxNumberOfRows();
  }

  /** Identifies the message as a Flush command. */
  interface Flush extends FrontendMessage {
  }

  /** Identifies the message as a function call. */
  interface FunctionCall extends FrontendMessage {

    /** Specifies the object ID of the function to call. */
    int id();

    /** The number of argument format codes that follow (denoted C below). This 
     * can be zero to indicate that there are no arguments or that the arguments 
     * all use the default format (text); or one, in which case the specified 
     * format code is applied to all arguments; or it can equal the actual number 
     * of arguments.
     * The argument format codes. Each must presently be zero (text) or one (binary).
     *  */
    short[] formatCodes();

    /** The length of the argument value, in bytes (this count does not include itself). 
     * Can be zero. As a special case, -1 indicates a NULL argument value. No value 
     * bytes follow in the NULL case.
     * The value of the argument, in the format indicated by the associated format code. 
     * n is the above length.
     *  */
    byte[][] fields();

    /** The format code for the function result. Must presently be zero (text) or 
     * one (binary). */
    short functionResultFormatCode();
  }

  /** Identifies the message as a function call result. */
  interface FunctionCallResponse extends BackendMessage {
    /** The length of the function result value, in bytes (this count does not include 
     * itself). Can be zero. As a special case, -1 indicates a NULL function result. 
     * No value bytes follow in the NULL case. */
    boolean isNull();

    /** The value of the function result, in the format indicated by the associated 
     * format code. n is the above length. */
    byte[] value();
  }

  /** Identifies the message as a no-data indicator. */
  interface NoData extends BackendMessage {
  }

  /** Identifies the message as a notice. */
  interface NoticeResponse extends BackendMessage {
    /** The message body consists of one or more identified fields, followed by a 
     * zero byte as a terminator. Fields can appear in any order. For each field 
     * there is the following: 
     * A code identifying the field type; if zero, this is the message terminator 
     * and no string follows. The presently defined field types are listed in 
     * Section 51.6. Since more field types might be added in future, frontends 
     * should silently ignore fields of unrecognized type.
     * */
    String[] fields();
  }

  /** Identifies the message as a notification response. */
  interface NotificationResponse extends BackendMessage {
    /** The process ID of the notifying backend process. */
    int processID();

    /** The name of the channel that the notify has been raised on. */
    String channelName();

    /** The "payload" string passed from the notifying process. */
    String payload();
  }

  /** Identifies the message as a parameter description */
  interface ParameterDescription extends BackendMessage {
    /** Then, for each parameter, there is the following: 
     * Specifies the object ID of the parameter data type. */
    int[] parameterDataType();
  }

  /** Identifies the message as a run-time parameter status report. */
  interface ParameterStatus extends BackendMessage {
    /** The name of the run-time parameter being reported. */
    String name();

    /** The current value of the parameter. */
    String value();
  }

  /** Identifies the message as a Parse command. */
  interface Parse extends FrontendMessage {
    /** The name of the destination prepared statement (an empty 
     * string selects the unnamed prepared statement). */
    String destinationName();

    /** The query string to be parsed. */
    String query();

    /** The number of parameter data types specified (can be zero). 
     * Note that this is not an indication of the number of parameters 
     * that might appear in the query string, only the number that the 
     * frontend wants to prespecify types for. 
     * Then, for each parameter, there is the following:
     * Specifies the object ID of the parameter data type. Placing a 
     * zero here is equivalent to leaving the type unspecified.
     * */
    int[] parameterTypes();
  }

  /** Identifies the message as a Parse-complete indicator. */
  interface ParseComplete extends BackendMessage {
  }

  /** Identifies the message as a password response. Note that this is also 
   * used for GSSAPI and SSPI response messages (which is really a design error, 
   * since the contained data is not a null-terminated string in that case, 
   * but can be arbitrary binary data). */
  interface PasswordMessage extends FrontendMessage {

    /** The password (encrypted, if requested). */
    String password();
  }

  /** Identifies the message as a portal-suspended indicator. Note this only appears 
   * if an Execute message's row-count limit was reached. */
  interface PortalSuspended extends BackendMessage {
  }

  /** Identifies the message as a simple query. */
  interface Query extends FrontendMessage {
    /** The query string itself. */
    String string();
  }

  /** Identifies the message type. ReadyForQuery is sent whenever the backend is ready 
   * for a new query cycle. */
  interface ReadyForQuery extends BackendMessage {

    /** Current backend transaction status indicator. Possible values are 'I' if idle 
     * (not in a transaction block); 'T' if in a transaction block; or 'E' if in a 
     * failed transaction block (queries will be rejected until block is ended). */
    char status();
  }

  interface RowDescription extends BackendMessage {
    interface Field {
      /** The field name. */
      String name();

      /** If the field can be identified as a column of a specific table, the object 
       * ID of the table; otherwise zero. */
      int objectID();

      /** If the field can be identified as a column of a specific table, the attribute 
       * number of the column; otherwise zero. */
      short attributeNumber();

      /** The object ID of the field's data type. */
      int dataType();

      /** The data type size (see pg_type.typlen). Note that negative values denote 
       * variable-width types. */
      short dataTypeSize();

      /** The type modifier (see pg_attribute.atttypmod). The meaning of the modifier is 
       * type-specific. */
      int typeModifier();

      /** The format code being used for the field. Currently will be zero (text) or one 
       * (binary). In a RowDescription returned from the statement variant of Describe, 
       * the format code is not yet known and will always be zero. */
      short formatCode();
    }
  }

  interface SSLRequest extends FrontendMessage {
  }

  interface StartupMessage extends FrontendMessage {
    interface Parameter {

      /** The parameter value. */
      String value();

      /** The database user name to connect as. Required; there is no default. */
      interface UserParameter extends Parameter {
      }

      /** The database to connect to. Defaults to the user name. */
      interface DatabaseParameter extends Parameter {
      }

      /** Command-line arguments for the backend. (This is deprecated in favor of 
       * setting individual run-time parameters.) Spaces within this string are 
       * considered to separate arguments, unless escaped with a backslash (\); 
       * write \\ to represent a literal backslash. */
      interface OptionsParameter extends Parameter {
      }
    }

    /** The protocol version number is followed by one or more pairs of parameter name 
     * and value strings. A zero byte is required as a terminator after the last 
     * name/value pair. Parameters can appear in any order. user is required, others 
     * are optional. Each parameter is specified as: */
    Parameter[] parameters();
  }

  /** Identifies the message as a Sync command. */
  interface Sync extends FrontendMessage {
  }

  /** Identifies the message as a termination. */
  interface Terminate extends FrontendMessage {
  }
}
