package io.trane.ndbc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.Value;

public class PreparedStatement {

  private final String query;
  private final Value<?>[] params;

  protected PreparedStatement(String query, Value<?>[] params) {
    super();
    this.query = query;
    this.params = params;
  }

  public PreparedStatement setInteger(int position, Integer value) {
    return setValue(position, value == null ? Value.NULL : new IntegerValue(value));
  }

  public PreparedStatement setString(int position, String value) {
    return setValue(position, value == null ? Value.NULL : new StringValue(value));
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
