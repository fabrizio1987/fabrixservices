package com.fabrix.flights.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fabrix.flights.config.FlightsServiceConfig;
import com.fabrix.flights.model.Customer;
import com.fabrix.flights.model.CustomerDetails;
import com.fabrix.flights.model.Flights;
import com.fabrix.flights.model.Hotels;
import com.fabrix.flights.model.Properties;
import com.fabrix.flights.repository.FlightsRepository;
import com.fabrix.flights.service.client.HotelsFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;



@RestController
public class FlightsController {
	
	@Autowired
	private FlightsRepository flightsRepository;
	
	@Autowired
	FlightsServiceConfig flightsConfig;
	
	@Autowired
	HotelsFeignClient hotelsFeignClient;

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
	

	@PostMapping("/myCustomerDetails")
	//@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod ="myCustomerDetailsFallBack")

	@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestBody Customer customer) {
		Flights flights = flightsRepository.findByCustomerId(customer.getCustomerId());
		List<Hotels> hotels = hotelsFeignClient.getHotelsDetails(customer);

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setFlights(flights);
		customerDetails.setHotels(hotels);
		
		return customerDetails;

	}
	
	private CustomerDetails myCustomerDetailsFallBack(Customer customer, Throwable t) {
		
		//fallback method that is used if Hotels services is down
		Flights flights = flightsRepository.findByCustomerId(customer.getCustomerId());
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setFlights(flights);
		return customerDetails;

	}
	
	@GetMapping("/sayHello")
	@RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Hello, Welcome to Fabrix app";
	}

	private String sayHelloFallback(Throwable t) {
		return "Hi, Welcome to Fabrix app";
	}

}
