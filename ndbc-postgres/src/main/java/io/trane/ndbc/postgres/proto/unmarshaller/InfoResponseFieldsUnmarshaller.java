package io.trane.ndbc.postgres.proto.unmarshaller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import io.trane.ndbc.postgres.proto.Message.InfoResponse;
import io.trane.ndbc.proto.BufferReader;

public final class InfoResponseFieldsUnmarshaller {

  private static final InfoResponse.Field[] emptyFieldArray = new InfoResponse.Field[0];

  private final Charset charset;

  public InfoResponseFieldsUnmarshaller(final Charset charset) {
    this.charset = charset;
  }

  public final InfoResponse.Field[] apply(final BufferReader b) {
    final List<InfoResponse.Field> fields = new ArrayList<>();
    byte type;
    while ((type = b.readByte()) != 0)
      fields.add(new InfoResponse.Field(toTypeEnum(type), b.readCString(charset)));
    return fields.toArray(emptyFieldArray);
  }

  private final InfoResponse.Field.Type toTypeEnum(final byte type) {
    switch (type) {
      case 'S':
        return InfoResponse.Field.Type.Severity;
      case 'V':
        return InfoResponse.Field.Type.Severity;
      case 'C':
        return InfoResponse.Field.Type.Code;
      case 'M':
        return InfoResponse.Field.Type.Message;
      case 'D':
        return InfoResponse.Field.Type.Detail;
      case 'H':
        return InfoResponse.Field.Type.Hint;
      case 'P':
        return InfoResponse.Field.Type.Position;
      case 'p':
        return InfoResponse.Field.Type.InternalPosition;
      case 'q':
        return InfoResponse.Field.Type.InternalQuery;
      case 'W':
        return InfoResponse.Field.Type.Where;
      case 's':
        return InfoResponse.Field.Type.SchemaName;
      case 't':
        return InfoResponse.Field.Type.TableName;
      case 'c':
        return InfoResponse.Field.Type.ColumnName;
      case 'd':
        return InfoResponse.Field.Type.DataTypeName;
      case 'n':
        return InfoResponse.Field.Type.ConstraintName;
      case 'F':
        return InfoResponse.Field.Type.File;
      case 'L':
        return InfoResponse.Field.Type.Line;
      case 'R':
        return InfoResponse.Field.Type.Routine;
      default:
        return InfoResponse.Field.Type.Unknown;
    }
  }
}
