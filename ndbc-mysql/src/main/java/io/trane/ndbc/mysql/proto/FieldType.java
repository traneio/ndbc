package io.trane.ndbc.mysql.proto;

import java.util.HashMap;
import java.util.Map;

public enum FieldType {

  BIT(16), BLOB(252), DATE(10), DATETIME(12), DECIMAL(0), NUMERIC(-10), DOUBLE(5), ENUM(247), FLOAT(4), GEOMETRY(
      255), INT24(9), LONG(3), LONG_BLOB(251), LONGLONG(8), MEDIUM_BLOB(250), NEW_DECIMAL(246), NEWDATE(14), NULL(
          6), SET(248), SHORT(
              2), STRING(254), TIME(11), TIMESTAMP(7), TINY(1), TINY_BLOB(249), VAR_STRING(253), VARCHAR(15), YEAR(13);

  private static final Map<Integer, FieldType> byCode;

  static {
    byCode = new HashMap<>();
    for (FieldType tpe : FieldType.values())
      byCode.put(tpe.code, tpe);
  }

  public static final FieldType apply(int code) {
    FieldType tpe = byCode.get(code);
    if (tpe == null)
      throw new UnsupportedOperationException("Unknown field type " + tpe);
    return tpe;
  }

  public final int code;

  private FieldType(int code) {
    this.code = code;
  }
}
