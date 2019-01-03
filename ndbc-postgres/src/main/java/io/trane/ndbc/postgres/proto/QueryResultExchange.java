package io.trane.ndbc.postgres.proto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.trane.ndbc.Row;
import io.trane.ndbc.postgres.encoding.EncodingRegistry;
import io.trane.ndbc.postgres.encoding.Format;
import io.trane.ndbc.postgres.proto.Message.CommandComplete;
import io.trane.ndbc.postgres.proto.Message.DataRow;
import io.trane.ndbc.postgres.proto.Message.EmptyQueryResponse;
import io.trane.ndbc.postgres.proto.Message.PortalSuspended;
import io.trane.ndbc.postgres.proto.Message.RowDescription;
import io.trane.ndbc.postgres.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.BufferReader;
import io.trane.ndbc.proto.Exchange;
import io.trane.ndbc.value.Value;

public final class QueryResultExchange {

  private final EncodingRegistry encoding;
  private final Unmarshallers    unmarshallers;

  public QueryResultExchange(final EncodingRegistry encoding, final Unmarshallers unmarshallers) {
    this.encoding = encoding;
    this.unmarshallers = unmarshallers;
  }

  public final Exchange<List<Row>> apply() {
    return Exchange.receive(unmarshallers.rowDescription).flatMap(this::apply);
  }

  public final Exchange<List<Row>> apply(RowDescription desc) {
    return gatherDataRows(new ArrayList<>()).map(rows -> toResultSet(desc, rows));
  }

  private final Row toRow(final EncodingRegistry encoding, final RowDescription desc, final DataRow data) {

    final RowDescription.Field[] fields = desc.fields;
    final BufferReader[] values = data.values;

    final int length = fields.length;
    final Map<String, Integer> positions = new HashMap<>(length);
    final Value<?>[] columns = new Value<?>[length];

    for (int i = 0; i < length; i++) {
      final RowDescription.Field field = fields[i];
      positions.put(field.name, i);
      final BufferReader reader = values[i];
      if (reader == null)
        columns[i] = Value.NULL;
      else {
        columns[i] = encoding.decode(field.dataType, Format.fromCode(field.formatCode), reader);
        reader.release();
      }
    }

    return Row.create(positions, columns);
  }

  private final Exchange<List<DataRow>> gatherDataRows(final List<DataRow> rows) {
    return Exchange.receive(
        unmarshallers.emptyQueryResponse.orElse(unmarshallers.commandComplete).orElse(unmarshallers.dataRow)
            .orElse(unmarshallers.portalSuspended))
        .flatMap(msg -> {
          if ((msg instanceof EmptyQueryResponse) || (msg instanceof CommandComplete)
              || (msg instanceof PortalSuspended))
            return Exchange.value(rows);
          else {
            rows.add((DataRow) msg);
            return gatherDataRows(rows);
          }
        });
  }

  private final List<Row> toResultSet(final RowDescription desc, final List<DataRow> dataRows) {
    final int size = dataRows.size();
    final List<Row> rows = new ArrayList<>(size);
    for (final DataRow dr : dataRows)
      rows.add(toRow(encoding, desc, dr));
    return rows;
  }
}
