package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class BalanceDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankInfo;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankInfoUrl;

	private boolean binding = false;

	private BigDecimal balance = BigDecimal.ZERO;

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(String bankInfo) {
		this.bankInfo = bankInfo;
	}

	public boolean isBinding() {
		return binding;
	}

	public void setBinding(boolean binding) {
		this.binding = binding;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getBankInfoUrl() {
		return bankInfoUrl;
	}

	public void setBankInfoUrl(String bankInfoUrl) {
		this.bankInfoUrl = bankInfoUrl;
	}

}