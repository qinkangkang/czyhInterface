package com.innee.czyhInterface.dto.m.activity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class activityDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String customerId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String code;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countPerson;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countCoupon;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String headImageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String firstOrderTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String status;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCountPerson() {
		return countPerson;
	}

	public void setCountPerson(String countPerson) {
		this.countPerson = countPerson;
	}

	public String getCountCoupon() {
		return countCoupon;
	}

	public void setCountCoupon(String countCoupon) {
		this.countCoupon = countCoupon;
	}

	public String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstOrderTime() {
		return firstOrderTime;
	}

	public void setFirstOrderTime(String firstOrderTime) {
		this.firstOrderTime = firstOrderTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}