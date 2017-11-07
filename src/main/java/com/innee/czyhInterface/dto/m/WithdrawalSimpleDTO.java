package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class WithdrawalSimpleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String withdrawalId;
	
	private Integer status = 10;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String applyer;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String applyTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String amount;

	public String getWithdrawalId() {
		return withdrawalId;
	}

	public void setWithdrawalId(String withdrawalId) {
		this.withdrawalId = withdrawalId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public String getApplyer() {
		return applyer;
	}

	public void setApplyer(String applyer) {
		this.applyer = applyer;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	public String getAccountTime() {
		return accountTime;
	}

	public void setAccountTime(String accountTime) {
		this.accountTime = accountTime;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}