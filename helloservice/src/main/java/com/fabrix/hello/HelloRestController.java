package com.fabrix.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {
	
	@GetMapping(value = "/hello")
	public String sayHello() {
		return "Hello from Fabrix Microservices";		
	}

}
