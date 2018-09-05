package io.trane.ndbc;

public class NdbcException extends RuntimeException {

  private static final long serialVersionUID = -7833706583000257638L;

  public NdbcException(final String msg) {
    super(msg);
  }

  public NdbcException(final String msg, final Exception cause) {
    super(msg, cause);
  }
}
