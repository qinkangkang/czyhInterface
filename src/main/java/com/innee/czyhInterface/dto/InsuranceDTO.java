package com.innee.czyhInterface.dto;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class InsuranceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String insured;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String idCard;

	public String getInsured() {
		return insured;
	}

	public void setInsured(String insured) {
		this.insured = insured;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

}