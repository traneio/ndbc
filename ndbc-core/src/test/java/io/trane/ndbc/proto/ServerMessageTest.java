package io.trane.ndbc.proto;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ServerMessageTest {

	@Test
	public void isError() {
		final ServerMessage msg = new ServerMessage() {
		};
		assertFalse(msg.isError());
	}

	@Test
	public void isNotice() {
		final ServerMessage msg = new ServerMessage() {
		};
		assertFalse(msg.isNotice());
	}
}
