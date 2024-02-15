package com.fabrix.flights.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fabrix.flights.model.Customer;


@Repository
public interface CustomerRepository extends CrudRepository<Customer, Integer> {

	Customer findByCustomerId(int customerId);

}
