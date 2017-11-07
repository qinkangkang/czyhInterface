package com.innee.czyhInterface.dto.m.system;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.EventSpecDTO;
import com.innee.czyhInterface.entity.TDictionaryRegion;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CityListDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer cityId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String cityName;

	private List<TDictionaryRegion> regionList;

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

	public List<TDictionaryRegion> getRegionList() {
		return regionList;
	}

	public void setRegionList(List<TDictionaryRegion> regionList) {
		this.regionList = regionList;
	}

}