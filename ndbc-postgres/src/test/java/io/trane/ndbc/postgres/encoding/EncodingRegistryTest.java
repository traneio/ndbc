package io.trane.ndbc.postgres.encoding;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.Set;

import org.junit.Test;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.util.Collections;
import io.trane.ndbc.value.IntegerValue;
import io.trane.ndbc.value.Value;

public class EncodingRegistryTest {

  @Test
  public void encode() {
    IntegerValue expected = new IntegerValue(13);
    EncodingRegistry reg = new EncodingRegistry(Optional.empty());
    ByteBuffer buf = ByteBuffer.allocate(1000);
    reg.encode(Format.BINARY, expected, new TestBufferWriter(buf));
    buf.rewind();
    IntegerValue actual = (new IntegerEncoding()).decodeBinary(new TestBufferReader(buf));
    assertEquals(expected, actual);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void encodeUnsupported() {
    Value<Thread> value = new Value<Thread>(new Thread()) {
    };
    EncodingRegistry reg = new EncodingRegistry(Optional.empty());
    ByteBuffer buf = ByteBuffer.allocate(1000);
    reg.encode(Format.BINARY, value, new TestBufferWriter(buf));
  }

  @Test
  public void decode() {
    IntegerValue value = new IntegerValue(213);
    EncodingRegistry reg = new EncodingRegistry(Optional.empty());
    ByteBuffer buf = ByteBuffer.allocate(1000);
    (new IntegerEncoding()).encode(Format.BINARY, value, new TestBufferWriter(buf));
    buf.rewind();
    Value<?> decoded = reg.decode(Oid.INT4, Format.BINARY, new TestBufferReader(buf));
    assertEquals(value, decoded);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void decodeUnsupported() {
    EncodingRegistry reg = new EncodingRegistry(Optional.empty());
    reg.decode(99999, Format.BINARY, new TestBufferReader(ByteBuffer.allocate(100)));
  }

  @Test
  public void customEncoding() {
    TestValueEncoding enc = new TestValueEncoding();
    TestValue value = new TestValue("str");
    EncodingRegistry reg = new EncodingRegistry(Optional.of(Collections.toImmutableSet(enc)));
    ByteBuffer buf = ByteBuffer.allocate(1000);
    reg.encode(Format.BINARY, value, new TestBufferWriter(buf));
    buf.limit(buf.position());
    buf.rewind();
    Value<?> decoded = reg.decode(enc.oids().iterator().next(), Format.BINARY,
        new TestBufferReader(buf));
    assertEquals(value, decoded);
  }

  class TestValue extends Value<String> {
    public TestValue(String value) {
      super(value);
    }

    @Override
    public String getString() {
      return get();
    }
  }

  class TestValueEncoding implements Encoding<TestValue> {

    @Override
    public Set<Integer> oids() {
      return Collections.toImmutableSet(9999);
    }

    @Override
    public Class<TestValue> valueClass() {
      return TestValue.class;
    }

    @Override
    public String encodeText(TestValue value) {
      return value.getString();
    }

    @Override
    public TestValue decodeText(String value) {
      return new TestValue(value);
    }

    @Override
    public void encodeBinary(TestValue value, BufferWriter b) {
      b.writeString(value.getString());
    }

    @Override
    public TestValue decodeBinary(BufferReader b) {
      return new TestValue(b.readString());
    }
  }
}
