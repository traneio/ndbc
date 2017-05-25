package io.trane.ndbc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.StringValue;
import io.trane.ndbc.value.Value;

public class PreparedStatement {

  private static final Value<?>[] emptyValues = new Value<?>[0];

  public static PreparedStatement apply(String query) {
    return new PreparedStatement(query, emptyValues);
  }

  private final String query;
  private final Value<?>[] params;

  private PreparedStatement(String query, Value<?>[] params) {
    super();
    this.query = query;
    this.params = params;
  }

  public PreparedStatement bind(Integer value) {
    return bind(value == null ? Value.NULL : new IntegerValue(value));
  }

  public PreparedStatement bind(String value) {
    return bind(value == null ? Value.NULL : new StringValue(value));
  }
  
  public PreparedStatement bindNull(String value) {
    return bind(Value.NULL);
  }

  public PreparedStatement bind(Value<?> param) {
    Value<?>[] newParams = Arrays.copyOf(params, params.length + 1);
    newParams[params.length] = param;
    return new PreparedStatement(query, newParams);
  }

  public String getQuery() {
    return query;
  }

  public List<Value<?>> getValues() {
    return Collections.unmodifiableList(Arrays.asList(params));
  }
}
