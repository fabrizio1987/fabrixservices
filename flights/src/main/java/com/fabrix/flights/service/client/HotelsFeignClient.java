package com.fabrix.flights.service.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import com.fabrix.flights.model.Customer;
import com.fabrix.flights.model.Hotels;


@FeignClient("hotels")
public interface HotelsFeignClient {

	@RequestMapping(method = RequestMethod.POST, value = "myHotels", consumes = "application/json")
	List<Hotels> getHotelsDetails(@RequestHeader("fabrix-correlation-id") String correlationid, @RequestBody Customer customer);
}

