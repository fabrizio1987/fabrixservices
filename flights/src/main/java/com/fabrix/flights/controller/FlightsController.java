package com.fabrix.flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fabrix.flights.model.Customer;
import com.fabrix.flights.model.Flights;
import com.fabrix.flights.repository.FlightsRepository;


@RestController
public class FlightsController {
	
	@Autowired
	private FlightsRepository flightsRepository;

	@PostMapping("/myFlights")
	public Flights getFlightsDetails(@RequestBody Customer customer) {

		Flights flights = flightsRepository.findByCustomerId(customer.getCustomerId());
		if (flights != null) {
			return flights;
		} else {
			return null;
		}

	}

}
