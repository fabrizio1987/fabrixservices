package com.fabrix;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
@EnableDiscoveryClient
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}
	
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
	    return builder.routes()
	        .route(p -> p
	            .path("/fabrix/flights/**") //predicate
	            .filters(f -> f.rewritePath("/fabrix/flights/(?<segment>.*)","/${segment}") //custom filter
	            				.addResponseHeader("X-Response-Time",new Date().toString())) //add resp header 
	            .uri("http://flights:8080")) // use the discovery service in kubernetes
	            //.uri("lb://FLIGHTS")) //redirect to the microservice registered in eureka as FLIGHTS
	        .route(p -> p
		            .path("/fabrix/hotels/**")
		            .filters(f -> f.rewritePath("/fabrix/hotels/(?<segment>.*)","/${segment}")
		            		.addResponseHeader("X-Response-Time",new Date().toString()))
		            .uri("http://hotels:8090")).build();
	}

}
