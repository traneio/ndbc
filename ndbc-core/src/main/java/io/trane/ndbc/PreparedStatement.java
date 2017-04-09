package io.trane.ndbc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PreparedStatement {

  private final String query;
  private final Value<?>[] params;

  protected PreparedStatement(String query, Value<?>[] params) {
    super();
    this.query = query;
    this.params = params;
  }

  public PreparedStatement setInt(int position, int value) {
    return setValue(position, new Value.IntegerValue(value));
  }

  public PreparedStatement setString(int position, String value) {
    return setValue(position, new Value.StringValue(value));
  }

  public PreparedStatement setValue(int position, Value<?> param) {
    int newLength = position >= params.length ? position + 1 : params.length;
    Value<?>[] newParams = Arrays.copyOf(params, newLength);
    newParams[position] = param;
    return new PreparedStatement(query, newParams);
  }

  public String getQuery() {
    return query;
  }

  public List<Value<?>> getValues() {
    return Collections.unmodifiableList(Arrays.asList(params));
  }
}
