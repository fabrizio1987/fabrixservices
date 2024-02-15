package com.fabrix.flights.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fabrix.flights.config.FlightsServiceConfig;
import com.fabrix.flights.model.Customer;
import com.fabrix.flights.model.CustomerDetails;
import com.fabrix.flights.model.Flights;
import com.fabrix.flights.model.Hotels;
import com.fabrix.flights.model.InfoMsgDto;
import com.fabrix.flights.model.Properties;
import com.fabrix.flights.repository.CustomerRepository;
import com.fabrix.flights.repository.FlightsRepository;
import com.fabrix.flights.service.client.HotelsFeignClient;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;

import org.springframework.cloud.stream.function.StreamBridge;



@RestController
@AllArgsConstructor
public class FlightsController {
	
	private static final Logger logger = LoggerFactory.getLogger(FlightsController.class);
	
	@Autowired
	private FlightsRepository flightsRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	FlightsServiceConfig flightsConfig;
	
	@Autowired
	HotelsFeignClient hotelsFeignClient;
	
	@Autowired
	private final StreamBridge streamBridge; //final???

	
	@PostMapping("/myFlights")
	public Flights getFlightsDetails(@RequestBody Customer customer) {
		logger.debug("getFlightsDetails method started");
		Flights flights = flightsRepository.findByCustomerId(customer.getCustomerId());
		logger.debug("getFlightsDetails method ended");
		if (flights != null) {
			return flights;
		} else {
			return null;
		}
		
	}
	
	@PostMapping("/myFlightsCommunication")
	public Flights getFlightsCommunication(@RequestBody Customer customer) {
		logger.debug("getFlightsDetails method started");
		Flights flights = flightsRepository.findByCustomerId(customer.getCustomerId());
		logger.debug("getFlightsDetails method ended");
		
		logger.debug("streamBridge " + streamBridge.toString());
		sendCommunication(customer);
		if (flights != null) {
			return flights;
		} else {
			return null;
		}
		
	}
	
	private void sendCommunication(Customer customer) {
        var infoMsgDto = new InfoMsgDto(customer.getCustomerId(), customer.getName(),
                customer.getEmail(), customer.getMobileNumber());
        logger.info("Sending Communication request for the details: {}", infoMsgDto);
        var result = streamBridge.send("sendCommunication-out-0", infoMsgDto);
        logger.info("Is the Communication request successfully triggered ? : {}", result);
    }
	
	public boolean updateCommunicationStatus(Integer customerId) {
        boolean isUpdated = false;
        if(customerId !=null ){
            Customer customer = customerRepository.findById(customerId).orElseThrow(
                    () -> new RuntimeException("not found - customerID " + customerId.toString())
            );
            customer.setCommunicationSw(true);
            customerRepository.save(customer);
            isUpdated = true;
        }
        return  isUpdated;
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
	@CircuitBreaker(name = "detailsForCustomerSupportApp", fallbackMethod ="myCustomerDetailsFallBack")
	@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomerDetailsFallBack")
	public CustomerDetails myCustomerDetails(@RequestHeader("fabrix-correlation-id") String correlationid, @RequestBody Customer customer) {
		logger.debug("myCustomerDetails() method started");
		Flights flights = flightsRepository.findByCustomerId(customer.getCustomerId());
		List<Hotels> hotels = hotelsFeignClient.getHotelsDetails(correlationid, customer);

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setFlights(flights);
		customerDetails.setHotels(hotels);
		logger.debug("myCustomerDetails() method ended");
		return customerDetails;

	}
	
	private CustomerDetails myCustomerDetailsFallBack(@RequestHeader("fabrix-correlation-id") String correlationid, Customer customer, Throwable t) {
		
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
