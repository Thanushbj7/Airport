package com.airport.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airport.model.Airport;

@Repository
public interface IAirportRepositary extends JpaRepository<Airport,Integer>{
	

}
