package io.trane.ndbc.spring;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;

import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;

public class NdbcTransactionManager implements PlatformTransactionManager {

  private final DataSource<PreparedStatement, Row> dataSource;

  public NdbcTransactionManager(final DataSource<PreparedStatement, Row> dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public TransactionStatus getTransaction(final TransactionDefinition definition) throws TransactionException {
    return null;
  }

  @Override
  public void commit(final TransactionStatus status) throws TransactionException {

  }

  @Override
  public void rollback(final TransactionStatus status) throws TransactionException {

  }
}
