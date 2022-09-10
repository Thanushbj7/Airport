package com.airport.model;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Airport {
	private String name;
	@Id
	@GeneratedValue(generator="airport_gen",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="airport_gen",sequenceName = "airport_seq",initialValue = 100,allocationSize = 1)
	private Integer id;
	private String category;
	@OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	
	private Address address;
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	
	private Set<Flight> flight;
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
	
	private Airline airline;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	
	public Airline getAirline() {
		return airline;
	}
	public void setAirline(Airline airline) {
		this.airline = airline;
	}
	
	
	public Airport(String name, String category, Address address, Set<Flight> flight, Airline airline) {
		super();
		this.name = name;
		this.category = category;
		this.address = address;
		this.flight = flight;
		this.airline = airline;
	}
	public Set<Flight> getFlight() {
		return flight;
	}
	public void setFlight(Set<Flight> flight) {
		this.flight = flight;
	}
	public Airport() {
		super();
		// TODO Auto-generated constructor stub
	}
	

}
