package com.innee.czyhInterface.dto.m.system.address;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ProvinceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer proId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String proName;

	private List<CityDTO> cityList = new ArrayList<CityDTO>();

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}

	public String getProName() {
		return proName;
	}

	public void setProName(String proName) {
		this.proName = proName;
	}

	public List<CityDTO> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityDTO> cityList) {
		this.cityList = cityList;
	}

}