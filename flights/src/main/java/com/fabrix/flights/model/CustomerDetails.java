package com.fabrix.flights.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CustomerDetails {

	private Flights flights;
	private List<Hotels> hotels;
}
