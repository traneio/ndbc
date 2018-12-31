package io.trane.ndbc.spring.example;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;

class Customer {
  @Id
  Long id;
  String name;
  LocalDate dob;
}