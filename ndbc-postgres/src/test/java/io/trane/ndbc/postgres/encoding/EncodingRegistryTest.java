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
    final IntegerValue expected = new IntegerValue(13);
    final EncodingRegistry reg = new EncodingRegistry(Optional.empty());
    final ByteBuffer buf = ByteBuffer.allocate(1000);
    reg.encode(Format.BINARY, expected, new TestBufferWriter(buf));
    buf.rewind();
    final IntegerValue actual = (new IntegerEncoding()).decodeBinary(new TestBufferReader(buf));
    assertEquals(expected, actual);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void encodeUnsupported() {
    final Value<Thread> value = new Value<Thread>(new Thread()) {
    };
    final EncodingRegistry reg = new EncodingRegistry(Optional.empty());
    final ByteBuffer buf = ByteBuffer.allocate(1000);
    reg.encode(Format.BINARY, value, new TestBufferWriter(buf));
  }

  @Test
  public void decode() {
    final IntegerValue value = new IntegerValue(213);
    final EncodingRegistry reg = new EncodingRegistry(Optional.empty());
    final ByteBuffer buf = ByteBuffer.allocate(1000);
    (new IntegerEncoding()).encode(Format.BINARY, value, new TestBufferWriter(buf));
    buf.rewind();
    final Value<?> decoded = reg.decode(Oid.INT4, Format.BINARY, new TestBufferReader(buf));
    assertEquals(value, decoded);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void decodeUnsupported() {
    final EncodingRegistry reg = new EncodingRegistry(Optional.empty());
    reg.decode(99999, Format.BINARY, new TestBufferReader(ByteBuffer.allocate(100)));
  }

  @Test
  public void customEncoding() {
    final TestValueEncoding enc = new TestValueEncoding();
    final TestValue value = new TestValue("str");
    final EncodingRegistry reg = new EncodingRegistry(Optional.of(Collections.toImmutableSet(enc)));
    final ByteBuffer buf = ByteBuffer.allocate(1000);
    reg.encode(Format.BINARY, value, new TestBufferWriter(buf));
    buf.limit(buf.position());
    buf.rewind();
    final Value<?> decoded = reg.decode(enc.oids().iterator().next(), Format.BINARY,
        new TestBufferReader(buf));
    assertEquals(value, decoded);
  }

  class TestValue extends Value<String> {
    public TestValue(final String value) {
      super(value);
    }

    @Override
    public String getString() {
      return get();
    }
  }

  class TestValueEncoding extends Encoding<TestValue> {

    @Override
    public Set<Integer> oids() {
      return Collections.toImmutableSet(9999);
    }

    @Override
    public Class<TestValue> valueClass() {
      return TestValue.class;
    }

    @Override
    public String encodeText(final TestValue value) {
      return value.getString();
    }

    @Override
    public TestValue decodeText(final String value) {
      return new TestValue(value);
    }

    @Override
    public void encodeBinary(final TestValue value, final BufferWriter b) {
      b.writeString(value.getString());
    }

    @Override
    public TestValue decodeBinary(final BufferReader b) {
      return new TestValue(b.readString());
    }
  }
}
