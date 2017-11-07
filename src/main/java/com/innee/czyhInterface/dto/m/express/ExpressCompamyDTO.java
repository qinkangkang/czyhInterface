package com.innee.czyhInterface.dto.m.express;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ExpressCompamyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer companyId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String companyName;

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

}