package com.airport.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Address {
	@Id
	@GeneratedValue(generator="address_gen",strategy=GenerationType.AUTO)
	@SequenceGenerator(name="address_gen",sequenceName = "address_gen",initialValue = 100,allocationSize = 1)
private Integer addressId;
private String city;
private String state;
private int pincode;

public int getAddressId() {
	return addressId;
}
public void setAddressId(int addressId) {
	this.addressId = addressId;
}
public String getCity() {
	return city;
}
public void setCity(String city) {
	this.city = city;
}
public String getState() {
	return state;
}
public void setState(String state) {
	this.state = state;
}
public int getPincode() {
	return pincode;
}
public void setPincode(int pincode) {
	this.pincode = pincode;
}
@Override
public String toString() {
	return "Address [city=" + city + ", state=" + state + ", pincode=" + pincode + "]";
}
public Address(String city, String state, int pincode) {
	super();
	this.city = city;
	this.state = state;
	this.pincode = pincode;
}
public Address() {
	super();
	// TODO Auto-generated constructor stub
}
}
