package com.innee.czyhInterface.dto.m.express;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ExpressDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String message;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String nu;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String ischeck;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String condition;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String com;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String status;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String state;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String phone;
	
	private TrackDTO[] data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public TrackDTO[] getData() {
		return data;
	}

	public void setData(TrackDTO[] data) {
		this.data = data;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}


}