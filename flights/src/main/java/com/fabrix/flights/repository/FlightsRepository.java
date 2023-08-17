package com.fabrix.flights.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fabrix.flights.model.Flights;


@Repository
public interface FlightsRepository extends CrudRepository<Flights, Long> {

	Flights findByCustomerId(int customerId);

}
