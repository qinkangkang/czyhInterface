package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class InsuranceSimpleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String commonInfoId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String insured;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String idCard;

	public String getCommonInfoId() {
		return commonInfoId;
	}

	public void setCommonInfoId(String commonInfoId) {
		this.commonInfoId = commonInfoId;
	}

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