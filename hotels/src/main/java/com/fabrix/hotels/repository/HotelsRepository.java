package com.fabrix.hotels.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fabrix.hotels.model.Hotels;

@Repository
public interface HotelsRepository extends CrudRepository<Hotels, Long> {

	
	List<Hotels> findByCustomerIdOrderByStartDtDesc(int customerId);

}

