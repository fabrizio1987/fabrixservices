package com.fabrix.flights.functions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fabrix.flights.controller.FlightsController;

import java.util.function.Consumer;

@Configuration
public class CustomerFunctions {
	
	private static final Logger log = LoggerFactory.getLogger(CustomerFunctions.class);

    @Bean
    public Consumer<Integer> updateCommunication(FlightsController flightsService) {
        return customerId -> {
            log.info("Updating Communication status for the customer : " + customerId.toString());
            flightsService.updateCommunicationStatus(customerId);
        };
    }

}
