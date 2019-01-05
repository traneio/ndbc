package io.trane.ndbc.spring.example;

import io.trane.ndbc.spring.repository.AsyncCrudRepository;

public interface AsyncCustomerRepository extends AsyncCrudRepository<Customer, Long> {
}
