package com.innee.czyhInterface.dto.m.user;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ShoppingAddressDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String id;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String customerId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String phone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String region;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String street;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;
	
	private Integer cityId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getDefaultstatus() {
		return defaultstatus;
	}

	public void setDefaultstatus(int defaultstatus) {
		this.defaultstatus = defaultstatus;
	}

	private int defaultstatus = 0;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

}