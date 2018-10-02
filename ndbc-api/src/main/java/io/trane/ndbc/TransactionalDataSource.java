package io.trane.ndbc;

import io.trane.future.Future;

public interface TransactionalDataSource extends DataSource {

  public Future<Void> commit();

  public Future<Void> rollback();
}
