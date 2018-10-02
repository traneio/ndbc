package io.trane.ndbc.completionStage;

import java.util.concurrent.CompletionStage;

public class TransactionalDataSource extends DataSource {

  private final io.trane.ndbc.TransactionalDataSource underlying;

  protected TransactionalDataSource(io.trane.ndbc.TransactionalDataSource underlying) {
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
