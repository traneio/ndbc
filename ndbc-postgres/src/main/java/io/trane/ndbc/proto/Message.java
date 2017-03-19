package io.trane.ndbc.proto;

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
}
