package com.backend.repository;

import org.springframework.context.annotation.Primary;

@SuppressWarnings("unused")
@Primary
public interface TestCustomerRepository extends CustomerRepository, CustomCustomerRepository {

}
