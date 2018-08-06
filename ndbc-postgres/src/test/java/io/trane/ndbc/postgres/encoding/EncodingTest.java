package io.trane.ndbc.postgres.encoding;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.junit.Assert;
import org.junit.Test;

import io.trane.ndbc.value.Value;

public abstract class EncodingTest<V extends Value<?>, E extends Encoding<?, V>> {

  private static final int SAMPLES = 1000;

  private final E                   enc;
  private final Integer             expectedOid;
  private final Class<V>            expectedValueClass;
  private final Function<Random, V> generator;
  private final BiConsumer<V, V>    verify;

  public EncodingTest(final E enc, final Integer expectedOid, final Class<V> expectedValueClass,
      final Function<Random, V> generator) {
    this(enc, expectedOid, expectedValueClass, generator, Assert::assertEquals);
  }

  public EncodingTest(final E enc, final Integer expectedOid, final Class<V> expectedValueClass,
      final Function<Random, V> generator, final BiConsumer<V, V> verify) {
    this.enc = enc;
    this.expectedOid = expectedOid;
    this.expectedValueClass = expectedValueClass;
    this.generator = generator;
    this.verify = verify;
  }

  @Test
  public void oi() {
    assertEquals(expectedOid, enc.oid());
  }

  @Test
  public void valueClass() {
    assertEquals(expectedValueClass, enc.valueClass());
  }

  private void testValue(final V value, final Format format) {
    final ByteBuffer buf = ByteBuffer.allocate(1000);
    enc.encode(format, value, new TestBufferWriter(buf));
    buf.limit(buf.position());
    buf.rewind();
    final V decoded = enc.decode(format, new TestBufferReader(buf));
    verify.accept(value, decoded);
  }

  private void testFormat(final Format format) {
    final Random r = new Random(1);
    for (int i = 0; i < SAMPLES; i++)
      testValue(generator.apply(r), format);
  }

  @Test
  public void textEncoding() {
    testFormat(Format.TEXT);
  }

  @Test
  public void binaryEncoding() {
    testFormat(Format.BINARY);
  }

  protected static LocalDateTime randomLocalDateTime(final Random r) {
    return LocalDateTime.of(r.nextInt(5000 - 1971) + 1971, r.nextInt(12) + 1, r.nextInt(28) + 1, r.nextInt(24),
        r.nextInt(60), r.nextInt(60), r.nextInt(99999) * 1000);
  }
}
