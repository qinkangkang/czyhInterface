package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class MerchantUserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String userId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String phone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankAccount;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String number;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String rate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String contractEffective;

	private boolean binding = false;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getContractEffective() {
		return contractEffective;
	}

	public void setContractEffective(String contractEffective) {
		this.contractEffective = contractEffective;
	}

	public boolean isBinding() {
		return binding;
	}

	public void setBinding(boolean binding) {
		this.binding = binding;
	}

}