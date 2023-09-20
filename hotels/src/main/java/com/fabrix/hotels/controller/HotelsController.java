package com.fabrix.hotels.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fabrix.hotels.config.HotelsServiceConfig;
import com.fabrix.hotels.model.Customer;
import com.fabrix.hotels.model.Hotels;
import com.fabrix.hotels.model.Properties;
import com.fabrix.hotels.repository.HotelsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;


@RestController
public class HotelsController {

	private static final Logger logger = LoggerFactory.getLogger(HotelsController.class);
	
	@Autowired
	private HotelsRepository hotelsRepository;
	
	@Autowired
	HotelsServiceConfig hotelsConfig;

	@PostMapping("/myHotels")
	public List<Hotels> getHotelsDetails(@RequestHeader("fabrix-correlation-id") String correlationid, @RequestBody Customer customer) {
		logger.info("getHotelsDetails() method started");
		List<Hotels> hotels = hotelsRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		logger.info("getHotelsDetails() method ended");
		if (hotels != null) {
			return hotels;
		} else {
			return null;
		}

	}
	
	
	@GetMapping("/hotels/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(hotelsConfig.getMsg(), hotelsConfig.getBuildVersion(),
				hotelsConfig.getMailDetails(), hotelsConfig.getActiveLocations());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}

}

