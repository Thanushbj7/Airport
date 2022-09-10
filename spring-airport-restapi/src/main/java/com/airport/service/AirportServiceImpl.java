package com.airport.service;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airport.model.Airport;
import com.airport.repositary.IAirportRepositary;
@Service
public class AirportServiceImpl implements IAirportService {

	@Autowired
    IAirportRepositary iAirportRepositary;

	@Override
	public void addAirport(Airport airport) {
		iAirportRepositary.save(airport);
		
	}

	@Override
	public void updateAirport(Airport airport) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAirportById(int id) {
		// TODO Auto-generated method stub
		
	}

	


	

}
