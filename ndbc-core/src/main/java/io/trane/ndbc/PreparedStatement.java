package io.trane.ndbc;

import java.util.Arrays;
import java.util.List;

public class PreparedStatement {

  private final String string;
  private final Param[] params;

  protected PreparedStatement(String string, Param[] params) {
    super();
    this.string = string;
    this.params = params;
  }

  private static interface Param {
  }

  private static interface IntParam extends Param {
    int value();
  }

  public PreparedStatement bind(int value) {
    return bind(new IntParam() {
      public int value() {
        return value;
      }
    });
  }

  private PreparedStatement bind(Param param) {
    int oldLength = params.length;
    Param[] newParams = Arrays.copyOf(params, oldLength + 1);
    newParams[oldLength] = param;
    return new PreparedStatement(string, newParams);
  }

}
