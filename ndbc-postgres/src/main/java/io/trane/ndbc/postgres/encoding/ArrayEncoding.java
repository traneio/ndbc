package io.trane.ndbc.postgres.encoding;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.BufferWriter;
import io.trane.ndbc.value.Value;

abstract class ArrayEncoding<I, V extends Value<I[]>> extends Encoding<I[], V> {

  private static final Pattern BRACES = Pattern.compile("\\{(.+?)\\}");
  private static final Pattern QUOTES = Pattern.compile("\"(.+?)\"");

  public ArrayEncoding(final Charset charset) {
    super(charset);
  }

  protected abstract I[] newArray(int length);

  protected abstract I[] emptyArray();

  protected abstract Encoding<I, ?> itemEncoding();

  @Override
  public final I[] decodeText(final String value) {
    final Matcher matcher = BRACES.matcher(value);

    if (matcher.find()) {
      final String[] pieces = matcher.group(1).split(",");
      final I[] result = newArray(pieces.length);

      for (int i = 0; i < result.length; i++)
        result[i] = decode(pieces[i]);

      return result;
    } else
      return emptyArray();
  }

  private final I decode(final String value) {
    final Matcher matcher = QUOTES.matcher(value);
    if (matcher.find())
      return itemEncoding().decodeText(matcher.group(1));
    else
      return itemEncoding().decodeText(value);
  }

  @Override
  public final void encodeBinary(final I[] value, final BufferWriter b) {
    b.writeInt(1); // dimensions
    b.writeInt(0); // flags
    b.writeInt(itemEncoding().oid());
    b.writeInt(value.length);
    b.writeInt(1); // lbound
    for (final I v : value)
      if (v == null)
        b.writeInt(-1);
      else {
        final int lengthPosition = b.writerIndex();
        b.writeInt(0); // length
        itemEncoding().encodeBinary(v, b);
        b.writeLengthNoSelf(lengthPosition);
      }
  }

  @Override
  public final I[] decodeBinary(final BufferReader b) {
    final int dimensions = b.readInt();
    assert dimensions <= 1;
    b.readInt(); // flags bit 0: 0=no-nulls, 1=has-nulls
    b.readInt(); // elementOid
    if (dimensions == 0)
      return emptyArray();
    else {
      final int length = b.readInt();
      final int lbound = b.readInt();
      assert lbound == 1;

      final I[] result = newArray(length);

      for (int i = 0; i < length; i++) {
        final int elemLength = b.readInt();
        if (elemLength == -1)
          result[i] = null;
        else
          result[i] = itemEncoding().decodeBinary(b.readSlice(elemLength));
      }
      return result;
    }
  }
}
