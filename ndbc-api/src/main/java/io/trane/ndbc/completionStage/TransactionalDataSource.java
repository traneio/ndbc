package io.trane.ndbc.completionStage;

import java.util.concurrent.CompletionStage;

import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public class TransactionalDataSource<P extends PreparedStatement, R extends Row> extends DataSource<P, R> {

  private final io.trane.ndbc.TransactionalDataSource<P, R> underlying;

  protected TransactionalDataSource(io.trane.ndbc.TransactionalDataSource<P, R> underlying) {
    super(underlying);
    this.underlying = underlying;
  }

  public CompletionStage<Void> commit() {
    return conv(underlying.commit());
  }

  public CompletionStage<Void> rollback() {
    return conv(underlying.rollback());
  }
}
