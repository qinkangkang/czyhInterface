package com.innee.czyhInterface.dto.m.user;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class MyCustomerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String customerId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer point;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer couponNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer level;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer growthValue;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer needGrowthValue;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Integer getCouponNum() {
		return couponNum;
	}

	public void setCouponNum(Integer couponNum) {
		this.couponNum = couponNum;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getGrowthValue() {
		return growthValue;
	}

	public void setGrowthValue(Integer growthValue) {
		this.growthValue = growthValue;
	}

	public Integer getNeedGrowthValue() {
		return needGrowthValue;
	}

	public void setNeedGrowthValue(Integer needGrowthValue) {
		this.needGrowthValue = needGrowthValue;
	}

}