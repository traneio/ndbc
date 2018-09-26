package io.trane.ndbc.sqlserver.proto.unmarshaller;

public class Unmarshallers {

  public PreLoginResponseUnmarshaller preLoginResponse;

  public Unmarshallers() {
    preLoginResponse = new PreLoginResponseUnmarshaller();
  }
}
