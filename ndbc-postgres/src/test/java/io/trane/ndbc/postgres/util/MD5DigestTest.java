package io.trane.ndbc.postgres.util;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;

import org.junit.Test;

public class MD5DigestTest {

	@Test
	public void encode() {
		final Charset charset = Charset.forName("UTF-8");
		final byte[] user = "foo".getBytes(charset);
		final byte[] password = "bar".getBytes(charset);
		final byte[] salt = "baz".getBytes(charset);

		final byte[] encoded = MD5Digest.encode(user, password, salt);
		assertEquals("md50143bd10979e4bd058f050b64eeb31ba", new String(encoded, charset));
	}
}
