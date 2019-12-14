package io.trane.ndbc.scala.stdlib;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import scala.concurrent.Future;

public class TransactionalDataSource<P extends PreparedStatement, R extends Row> extends DataSource<P, R> {

  private final io.trane.ndbc.TransactionalDataSource<P, R> underlying;

  protected TransactionalDataSource(final io.trane.ndbc.TransactionalDataSource<P, R> underlying) {
    super(underlying);
    this.underlying = underlying;
  }

  public Future<Void> commit() {
    return convert(underlying.commit());
  }

  public Future<Void> rollback() {
    return convert(underlying.rollback());
  }
}