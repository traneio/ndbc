package io.trane.ndbc.proto;

import org.junit.Test;
import static org.junit.Assert.*;

public class ServerMessageTest {

  @Test
  public void isError() {
    ServerMessage msg = new ServerMessage() {
    };
    assertFalse(msg.isError());
  }

  @Test
  public void isNotice() {
    ServerMessage msg = new ServerMessage() {
    };
    assertFalse(msg.isNotice());
  }
}
