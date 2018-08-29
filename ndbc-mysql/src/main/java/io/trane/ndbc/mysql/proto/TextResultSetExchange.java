package io.trane.ndbc.mysql.proto;

import java.util.ArrayList;
import java.util.List;

import io.trane.ndbc.mysql.proto.Message.EofPacket;
import io.trane.ndbc.mysql.proto.Message.Field;
import io.trane.ndbc.mysql.proto.Message.OkPacket;
import io.trane.ndbc.mysql.proto.Message.TextResultSet;
import io.trane.ndbc.mysql.proto.Message.TextRow;
import io.trane.ndbc.mysql.proto.unmarshaller.TextRowUnmarshaller;
import io.trane.ndbc.mysql.proto.unmarshaller.Unmarshallers;
import io.trane.ndbc.proto.Exchange;

public class TextResultSetExchange {

  private final Unmarshallers unmarshallers;

  public TextResultSetExchange(Unmarshallers unmarshallers) {
    this.unmarshallers = unmarshallers;
  }

  public Exchange<TextResultSet> apply() {
    return Exchange.receive(unmarshallers.columnCount)
        .map(c -> (int) c.count)
        .flatMap(c -> fields(c, new ArrayList<Field>(c))
            .flatMap(fields -> textRows(unmarshallers.textRow(fields), new ArrayList<TextRow>())
                .map(rows -> new TextResultSet(fields, rows))));
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

  private Exchange<List<TextRow>> textRows(TextRowUnmarshaller u, List<TextRow> l) {
    return Exchange.receive(unmarshallers.terminator.orElse(u)).flatMap(msg -> {
      if (msg instanceof OkPacket || msg instanceof EofPacket)
        return Exchange.value(l);
      else {
        l.add((TextRow) msg);
        return textRows(u, l);
      }
    });
  }
}
