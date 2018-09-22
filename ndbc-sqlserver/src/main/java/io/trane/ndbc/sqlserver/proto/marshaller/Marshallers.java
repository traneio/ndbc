package io.trane.ndbc.sqlserver.proto.marshaller;

public class Marshallers {

  public PreLoginMarshaller preLogin;
  public AttentionMarshaller attention;

  public Marshallers() {
    preLogin = new PreLoginMarshaller();
    attention = new AttentionMarshaller();
  }
}
