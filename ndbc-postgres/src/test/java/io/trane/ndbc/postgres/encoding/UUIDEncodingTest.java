package io.trane.ndbc.postgres.encoding;

import java.util.UUID;

import io.trane.ndbc.value.UUIDValue;

public class UUIDEncodingTest extends EncodingTest<UUIDValue, UUIDEncoding> {

  public UUIDEncodingTest() {
    super(new UUIDEncoding(UTF8), Oid.UUID, UUIDValue.class, r -> new UUIDValue(UUID.randomUUID()));
  }
}