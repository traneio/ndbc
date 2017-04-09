package io.trane.ndbc;

import java.util.Iterator;

public final class ResultSet implements Iterable<Row> {

  private final Iterator<Row> iterator;

  public ResultSet(Iterator<Row> iterator) {
    super();
    this.iterator = iterator;
  }

  @Override
  public Iterator<Row> iterator() {
    return iterator;
  }
}
