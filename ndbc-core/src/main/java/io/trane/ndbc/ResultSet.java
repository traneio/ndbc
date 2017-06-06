package io.trane.ndbc;

import java.util.Iterator;
import java.util.List;

public final class ResultSet implements Iterable<Row> {

  private final List<Row> rows;

  public ResultSet(final List<Row> rows2) {
    super();
    this.rows = rows2;
  }

  @Override
  public final Iterator<Row> iterator() {
    return rows.iterator();
  }
}
