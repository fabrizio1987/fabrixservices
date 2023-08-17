package com.fabrix.hotels.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fabrix.hotels.model.Customer;
import com.fabrix.hotels.model.Hotels;
import com.fabrix.hotels.repository.HotelsRepository;


@RestController
public class HotelsController {

	@Autowired
	private HotelsRepository hotelsRepository;

	@PostMapping("/myHotels")
	public List<Hotels> getLoansDetails(@RequestBody Customer customer) {
		List<Hotels> hotels = hotelsRepository.findByCustomerIdOrderByStartDtDesc(customer.getCustomerId());
		if (hotels != null) {
			return hotels;
		} else {
			return null;
		}

	}

}

