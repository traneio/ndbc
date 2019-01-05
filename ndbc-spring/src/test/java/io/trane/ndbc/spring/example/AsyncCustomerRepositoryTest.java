package io.trane.ndbc.spring.example;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import io.trane.future.CheckedFutureException;
import io.trane.ndbc.spring.repository.config.NdbcConfiguration;

@RunWith(SpringRunner.class)
@Transactional
@ContextConfiguration(classes = NdbcConfiguration.class)
public class AsyncCustomerRepositoryTest {

  @Autowired
  AsyncCustomerRepository repo;

  @Test
  public void createSimpleCustomer() throws CheckedFutureException {
    final Customer customer = new Customer();
    customer.dob = LocalDate.of(1904, 5, 14);
    customer.name = "Albert";
    final Duration timeout = Duration.ofSeconds(10);

    final Customer saved = repo.save(customer).get(timeout);

    assertThat(saved.id).isNotNull();

    saved.name = "Hans Albert";

    repo.save(saved);

    final Optional<Customer> reloaded = repo.findById(saved.id).get(timeout);

    assertThat(reloaded).isNotEmpty();

    assertThat(reloaded.get().name).isEqualTo("Hans Albert");
  }
}
