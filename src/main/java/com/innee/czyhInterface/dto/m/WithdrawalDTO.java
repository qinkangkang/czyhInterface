package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class WithdrawalDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String withdrawalId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankInfo;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String applyTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String accountTime;

	private BigDecimal amount = BigDecimal.ZERO;

	public String getWithdrawalId() {
		return withdrawalId;
	}

	public void setWithdrawalId(String withdrawalId) {
		this.withdrawalId = withdrawalId;
	}

	public String getBankInfo() {
		return bankInfo;
	}

	public void setBankInfo(String bankInfo) {
		this.bankInfo = bankInfo;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public String getAccountTime() {
		return accountTime;
	}

	public void setAccountTime(String accountTime) {
		this.accountTime = accountTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

}