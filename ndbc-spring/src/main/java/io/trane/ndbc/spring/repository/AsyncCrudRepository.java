package io.trane.ndbc.spring.repository;

import java.util.Optional;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import io.trane.future.Future;

@NoRepositoryBean
public interface AsyncCrudRepository<T, ID> extends Repository<T, ID> {

  <S extends T> Future<S> save(S entity);

  <S extends T> Future<Iterable<S>> saveAll(Iterable<S> entities);

  Future<Optional<T>> findById(ID id);

  Future<Boolean> existsById(ID id);

  Future<Iterable<T>> findAll();

  Future<Iterable<T>> findAllById(Iterable<ID> ids);

  Future<Long> count();

  void deleteById(ID id);

  void delete(T entity);

  void deleteAll(Iterable<? extends T> entities);

  void deleteAll();
}