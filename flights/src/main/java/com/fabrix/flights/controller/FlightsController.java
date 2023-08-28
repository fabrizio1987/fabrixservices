package com.fabrix.flights.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fabrix.flights.config.FlightsServiceConfig;
import com.fabrix.flights.model.Customer;
import com.fabrix.flights.model.Flights;
import com.fabrix.flights.model.Properties;
import com.fabrix.flights.repository.FlightsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;



@RestController
public class FlightsController {
	
	@Autowired
	private FlightsRepository flightsRepository;
	
	@Autowired
	FlightsServiceConfig flightsConfig;

	@PostMapping("/myFlights")
	public Flights getFlightsDetails(@RequestBody Customer customer) {

		Flights flights = flightsRepository.findByCustomerId(customer.getCustomerId());
		if (flights != null) {
			return flights;
		} else {
			return null;
		}

	}
	
	@GetMapping("/flights/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(flightsConfig.getMsg(), flightsConfig.getBuildVersion(),
				flightsConfig.getMailDetails(), flightsConfig.getActiveLocations());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}

}
