package io.trane.ndbc.spring.example;

import io.trane.ndbc.spring.repository.AsyncCrudRepository;

public interface CustomerRepository extends AsyncCrudRepository<Customer, Long> {
}