package io.trane.ndbc.mysql.proto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import io.trane.ndbc.NdbcException;
import io.trane.ndbc.mysql.proto.Message.ColumnCount;
import io.trane.ndbc.mysql.proto.Message.EofPacket;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.OkPacket;
import io.trane.ndbc.mysql.proto.Message.Row;
import io.trane.ndbc.mysql.proto.unmarshaller.MysqlUnmarshaller;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public class ResultSetExchange {

  private final Unmarshallers unmarshallers;

  private static final Exchange<List<io.trane.ndbc.Row>> emptyRows = Exchange
      .value(Collections.unmodifiableList(new ArrayList<>()));

  public ResultSetExchange(Unmarshallers unmarshallers) {
    this.unmarshallers = unmarshallers;
  }

  public Exchange<List<io.trane.ndbc.Row>> apply(boolean binary) {
    return Exchange.receive(unmarshallers.terminator.orElse(unmarshallers.columnCount)).flatMap(msg -> {
      if (msg instanceof ColumnCount) {
        int count = (int) ((ColumnCount) msg).count;
        return fields(count, new ArrayList<Field>(count))
            .flatMap(fields -> rows(unmarshallers.row(fields, binary), new ArrayList<Row>())
                .map(rows -> handleResultSet(fields, rows)));
      } else if (msg instanceof OkPacket) {
        return emptyRows;
      } else
        throw new NdbcException(msg.toString());
    });
  }

  private Exchange<List<Field>> fields(int columns, List<Field> l) {
    if (columns == 0)
      return Exchange.receive(unmarshallers.terminator).map(t -> l);
    else
      return Exchange.receive(unmarshallers.field).flatMap(f -> {
        l.add(f);
        return fields(columns - 1, l);
      });
  }

  private List<io.trane.ndbc.Row> handleResultSet(List<Field> fields, List<Row> rows) {
    final AtomicInteger index = new AtomicInteger();
    final Map<String, Integer> positions = fields.stream()
        .collect(Collectors.toMap(t -> t.name, any -> index.getAndIncrement()));
    final List<io.trane.ndbc.Row> result = rows.stream().map(row -> io.trane.ndbc.Row.apply(positions, row.values))
        .collect(Collectors.toList());
    return result;
  }

  private Exchange<List<Row>> rows(MysqlUnmarshaller<Row> u, List<Row> l) {
    return Exchange.receive(u.orElse(unmarshallers.terminator)).flatMap(msg -> {
      if (msg instanceof OkPacket || msg instanceof EofPacket)
        return Exchange.value(l);
      else {
        l.add((Row) msg);
        return rows(u, l);
      }
    });
  }
}
