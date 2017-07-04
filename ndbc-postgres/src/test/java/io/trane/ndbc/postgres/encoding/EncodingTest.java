package io.trane.ndbc.postgres.encoding;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;

import org.junit.Test;

import io.trane.ndbc.value.Value;

public abstract class EncodingTest<V extends Value<?>, E extends Encoding<V>> {

  private static final int          SAMPLES = 1000;

  private final E                   enc;
  private final Set<Integer>        expectedOids;
  private final Class<V>            expectedValueClass;
  private final Function<Random, V> generator;

  public EncodingTest(E enc, Set<Integer> expectedOids,
      Class<V> expectedValueClass, Function<Random, V> generator) {
    super();
    this.enc = enc;
    this.expectedOids = expectedOids;
    this.expectedValueClass = expectedValueClass;
    this.generator = generator;
  }

  @Test
  public void iods() {
    assertEquals(expectedOids, enc.oids());
  }

  @Test
  public void valueClass() {
    assertEquals(expectedValueClass, enc.valueClass());
  }

  @Test
  public void textEncoding() {
    Random r = new Random(1);
    for (int i = 0; i < SAMPLES; i++) {
      V expected = generator.apply(r);
      String encoded = enc.encodeText(expected);
      System.out.println(encoded);
      V actual = enc.decodeText(encoded);
      assertEquals(expected, actual);
    }
  }

  @Test
  public void binaryEncoding() {
    Random r = new Random(1);
    for (int i = 0; i < SAMPLES; i++) {
      ByteBuffer buf = ByteBuffer.allocate(1000);
      V expected = generator.apply(r);
      enc.encodeBinary(expected, new TestBufferWriter(buf));
      buf.limit(buf.position());
      buf.rewind();
      V actual = enc.decodeBinary(new TestBufferReader(buf));
      assertEquals(expected, actual);
    }
  }

  protected static LocalDateTime randomLocalDateTime(final Random r) {
    return LocalDateTime.of(r.nextInt(5000 - 1971) + 1971, r.nextInt(12) + 1, r.nextInt(28) + 1,
        r.nextInt(24),
        r.nextInt(60),
        r.nextInt(60), r.nextInt(99999) * 1000);
  }
}
