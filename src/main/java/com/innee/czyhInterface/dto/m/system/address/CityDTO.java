package com.innee.czyhInterface.dto.m.system.address;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CityDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer cityId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String cityName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String isHot;

	private List<RegionDTO> regionList;

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getIsHot() {
		return isHot;
	}

	public void setIsHot(String isHot) {
		this.isHot = isHot;
	}

	public List<RegionDTO> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<RegionDTO> regionList) {
		this.regionList = regionList;
	}

}