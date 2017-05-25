package io.trane.ndbc;

import java.util.Iterator;
import java.util.stream.Stream;

public final class ResultSet implements Iterable<Row> {

  private final Stream<Row> stream;

  public ResultSet(Stream<Row> rows) {
    super();
    this.stream = rows;
  }

  @Override
  public Iterator<Row> iterator() {
    return stream.iterator();
  }
}
