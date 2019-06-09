package org.springframework.data.jdbc.core;

import java.util.Collections;
import java.util.Optional;

import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;

import io.trane.future.Future;
import io.trane.ndbc.DataSource;
import io.trane.ndbc.PreparedStatement;
import io.trane.ndbc.Row;
import io.trane.ndbc.spring.repository.AsyncCrudRepository;

public class SimpleNdbcRepository<T, ID> implements AsyncCrudRepository<T, ID> {

  private final DataSource<PreparedStatement, Row> dataSource;
  private final SqlGeneratorSource sqlGeneratorSource;
  private final RelationalPersistentEntity<T> entity;

  public SimpleNdbcRepository(final DataSource<PreparedStatement, Row> dataSource,
      final SqlGeneratorSource sqlGeneratorSource, final RelationalPersistentEntity<T> entity) {
    this.dataSource = dataSource;
    this.sqlGeneratorSource = sqlGeneratorSource;
    this.entity = entity;
  }

  @Override
  public <S extends T> Future<S> save(final S entity) {
    final String statement = sql(entity.getClass()).getInsert(Collections.emptySet());
    System.out.println(statement);
    return dataSource.execute("INSERT INTO customer (name) VALUES ('jão')").flatMap(rows -> {
      return Future.exception(new Exception("AGORA VAI!"));
    });
  }

  @Override
  public <S extends T> Future<Iterable<S>> saveAll(final Iterable<S> entities) {
    return null;
  }

  @Override
  public Future<Optional<T>> findById(final ID id) {
    return null;
  }

  @Override
  public Future<Boolean> existsById(final ID id) {
    return null;
  }

  @Override
  public Future<Iterable<T>> findAll() {
    return null;
  }

  @Override
  public Future<Iterable<T>> findAllById(final Iterable<ID> ids) {
    return null;
  }

  @Override
  public Future<Long> count() {
    return null;
  }

  @Override
  public void deleteById(final ID id) {
  }

  @Override
  public void delete(final T entity) {
  }

  @Override
  public void deleteAll(final Iterable<? extends T> entities) {
  }

  @Override
  public void deleteAll() {
  }

  private SqlGenerator sql(final Class<?> domainType) {
    return sqlGeneratorSource.getSqlGenerator(domainType);
  }
}
