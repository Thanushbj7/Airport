package com.airport.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Flight {
	@Id
	@GeneratedValue(generator="flight_seq",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="flight_seq",sequenceName = "flight_seq",initialValue = 100,allocationSize = 1)
	private Integer flightId;
	private String flightName;
	private double price;
	
	@Override
	public String toString() {
		return "Flight [flightName=" + flightName + ", price=" + price + "]";
	}
	public int getFlightId() {
		return flightId;
	}
	public void setFlightId(int flightId) {
		this.flightId = flightId;
	}
	public String getFlightName() {
		return flightName;
	}
	public Flight(String flightName, double price) {
		super();
		this.flightName = flightName;
		this.price = price;
	}
	public Flight() {
		super();
		// TODO Auto-generated constructor stub
	}
	public void setFlightName(String flightName) {
		this.flightName = flightName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
}
