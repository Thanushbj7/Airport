package com.airport.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Airline {
private String airlineName;
@Id
@GeneratedValue(generator="airline_gen",strategy=GenerationType.AUTO)
@SequenceGenerator(name="airline_gen",sequenceName = "airline_gen",initialValue = 100,allocationSize = 1)
private Integer airlineId;
private String airlineType;
public String getAirlineName() {
	return airlineName;
}
public void setAirlineName(String airlineName) {
	this.airlineName = airlineName;
}
public int getAirlineId() {
	return airlineId;
}
public void setAirlineId(int airlineId) {
	this.airlineId = airlineId;
}
public String getAirlineType() {
	return airlineType;
}
public void setAirlineType(String airlineType) {
	this.airlineType = airlineType;
}
public Airline(String airlineName, String airlineType) {
	super();
	this.airlineName = airlineName;
	this.airlineType = airlineType;
}
public Airline() {
	super();
	// TODO Auto-generated constructor stub
}
@Override
public String toString() {
	return "Airline [airlineName=" + airlineName + ", airlineType=" + airlineType + "]";
}
}
