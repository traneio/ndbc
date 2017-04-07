package io.trane.ndbc.proto;

public interface ServerMessage {
  
  default boolean isError() {
    return false;
  }
  
  default boolean isNotice() {
    return false;
  }
}
