package io.trane.ndbc.mysql.proto.unmarshaller;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;

import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.PacketBufferReader;

public class FieldUnmarshaller extends MysqlUnmarshaller<Field> {

  /**
   * SELECT id,collation_name FROM information_schema.collations WHERE
   * `collation_name` LIKE 'latin1%' ORDER BY id;
   */
  private static final List<Integer> Latin1Set = Arrays.asList(5, 8, 15, 31, 47, 48, 49, 94);

  /**
   * "SELECT id,collation_name FROM information_schema.collations WHERE
   * collation_name LIKE '%utf8' ORDER BY id"
   */
  private static final List<Integer> Utf8Set = Arrays.asList(33, 45, 46, 83, 192, 193, 194, 195, 196, 197, 198, 199,
      200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222,
      223, 224, 225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241, 242, 243, 244, 245,
      246, 247, 248, 249, 250, 251, 252, 253, 254);

  /**
   * @see https://dev.mysql.com/doc/refman/5.7/en/charset-binary-set.html
   */
  private static final short Binary = 63;

  private final Charset charset;

  /**
   * Converts from mysql charset to java charset.
   */
  public static final Charset charset(short charset) {
    if (Utf8Set.contains(charset))
      return UTF_8;
    else if (Latin1Set.contains(charset))
      return ISO_8859_1;
    else if (charset == Binary)
      return US_ASCII;
    else
      return US_ASCII;
  }

  public FieldUnmarshaller(final Charset charset) {
    this.charset = charset;
  }

  @Override
  public Field decode(final PacketBufferReader p) {
    final byte[] bytesCatalog = p.readLengthCodedBytes();
    final byte[] bytesDb = p.readLengthCodedBytes();
    final byte[] bytesTable = p.readLengthCodedBytes();
    final byte[] bytesOrigTable = p.readLengthCodedBytes();
    final byte[] bytesName = p.readLengthCodedBytes();
    final byte[] bytesOrigName = p.readLengthCodedBytes();
    p.readVariableLong(); // length of the following fields (always 0x0c)
    final int columnCharset = p.readUnsignedShort();
    final String catalog = new String(bytesCatalog, charset);
    final String db = new String(bytesDb, charset);
    final String table = new String(bytesTable, charset);
    final String origTable = new String(bytesOrigTable, charset);
    final String name = new String(bytesName, charset);
    final String origName = new String(bytesOrigName, charset);
    final long length = p.readUnsignedInt();
    final int fieldType = p.readByte() & 0xFF;
    final int flags = p.readUnsignedShort();
    final int decimals = p.readByte() & 0xFF;
    return new Field(catalog, db, table, origTable, name, origName, charset((short) columnCharset), length, fieldType,
        flags,
        decimals);
  }
}
