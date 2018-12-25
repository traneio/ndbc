package io.trane.ndbc;

import io.trane.future.Future;

/**
 * This class represents a data source pinned to a connection with an open
 * transaction. Make sure to invoke `close` once the transaction is done so the
 * connection can return to the pool
 */
public interface TransactionalDataSource<P extends PreparedStatement, R extends Row> extends DataSource<P, R> {

  public Future<Void> commit();

  public Future<Void> rollback();
}
