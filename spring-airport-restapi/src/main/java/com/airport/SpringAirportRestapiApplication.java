package com.airport;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.airport.model.Address;
import com.airport.model.Airline;
import com.airport.model.Airport;
import com.airport.model.Flight;
import com.airport.service.AirportServiceImpl;
import com.airport.service.IAirportService;

@SpringBootApplication
public class SpringAirportRestapiApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(SpringAirportRestapiApplication.class, args);
	}
	@Autowired
AirportServiceImpl airportServiceImpl;
	@Override
	public void run(String... args) throws Exception {
//		Address address=new Address("Banglore","Karnataka",12345);
//		Airline airline=new Airline("AirIndia","American Airline");
//		Flight flight=new Flight("AirBus 319",100000);
//		Set<Flight> flightList=new HashSet<>(Arrays.asList(flight));
//		Airport airport=new Airport("Devnalli Airport","Commercial",address,flightList,airline);
//		airportServiceImpl.addAirport(airport);
	}

}
