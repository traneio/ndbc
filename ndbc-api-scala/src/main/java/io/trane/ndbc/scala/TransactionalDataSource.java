package io.trane.ndbc.scala;

import scala.concurrent.Future;

public class TransactionalDataSource extends DataSource {

  private final io.trane.ndbc.TransactionalDataSource underlying;

  protected TransactionalDataSource(final io.trane.ndbc.TransactionalDataSource underlying) {
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