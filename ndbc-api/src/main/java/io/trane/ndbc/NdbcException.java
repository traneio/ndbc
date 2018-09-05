package io.trane.ndbc;

public class NdbcException extends RuntimeException {

  private static final long serialVersionUID = -7833706583000257638L;

  public NdbcException(String msg) {
    super(msg);
  }

  public NdbcException(String msg, Exception cause) {
    super(msg, cause);
  }
}
