package io.trane.ndbc;

import java.util.Arrays;

public class PreparedStatement {

  private final String string;
  private final Param[] params;

  protected PreparedStatement(String string, Param[] params) {
    super();
    this.string = string;
    this.params = params;
  }

  public static interface Param {
  }

  public static interface IntParam extends Param {
    int value();
  }

  public PreparedStatement setInt(int position, int value) {
    return setParam(position, new IntParam() {
      public int value() {
        return value;
      }
    });
  }

  public static interface StringParam extends Param {
    String value();
  }

  public PreparedStatement setString(int position, String value) {
    return setParam(position, new StringParam() {
      public String value() {
        return value;
      }
    });
  }

  public PreparedStatement setParam(int position, Param param) {
    int newLength = position >= params.length ? position + 1 : params.length;
    Param[] newParams = Arrays.copyOf(params, newLength);
    newParams[position] = param;
    return new PreparedStatement(string, newParams);
  }
}
