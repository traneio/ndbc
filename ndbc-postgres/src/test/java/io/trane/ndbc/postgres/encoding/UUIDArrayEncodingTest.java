package io.trane.ndbc.postgres.encoding;

import java.util.Arrays;
import java.util.UUID;

import org.junit.Assert;

import io.trane.ndbc.postgres.value.UUIDArrayValue;

public class UUIDArrayEncodingTest extends EncodingTest<UUIDArrayValue, UUIDArrayEncoding> {

  public UUIDArrayEncodingTest() {
    super(new UUIDArrayEncoding(new UUIDEncoding(UTF8), UTF8), Oid.UUID_ARRAY, UUIDArrayValue.class, r -> {
      final UUID[] uuids = new UUID[r.nextInt(8)];
      Arrays.setAll(uuids, p -> UUID.randomUUID());

      return new UUIDArrayValue(uuids);
    }, (a, b) -> Assert.assertArrayEquals(a.getUUIDArray(), b.getUUIDArray()));
  }
}