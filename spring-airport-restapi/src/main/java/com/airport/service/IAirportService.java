package com.airport.service;


import org.springframework.stereotype.Service;

import com.airport.model.Airport;

@Service
public interface IAirportService {

	void addAirport(Airport airport);

	void updateAirport(Airport airport);

	void deleteAirportById(int id);
}
