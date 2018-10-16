package io.trane.ndbc;

import io.trane.future.Future;

public interface TransactionalDataSource<P extends PreparedStatement, R extends Row> extends DataSource<P, R> {

  public Future<Void> commit();

  public Future<Void> rollback();
}
