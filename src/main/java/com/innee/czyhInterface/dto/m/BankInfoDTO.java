package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class BankInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorId;

	private int bankId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankAccount;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankAccountName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankAccountPersonId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankPhone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bankImage;

	private boolean binding = false;

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public String getBankAccountPersonId() {
		return bankAccountPersonId;
	}

	public void setBankAccountPersonId(String bankAccountPersonId) {
		this.bankAccountPersonId = bankAccountPersonId;
	}

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public boolean isBinding() {
		return binding;
	}

	public void setBinding(boolean binding) {
		this.binding = binding;
	}

	public int getBankId() {
		return bankId;
	}

	public void setBankId(int bankId) {
		this.bankId = bankId;
	}

	public String getBankPhone() {
		return bankPhone;
	}

	public void setBankPhone(String bankPhone) {
		this.bankPhone = bankPhone;
	}

	public String getBankImage() {
		return bankImage;
	}

	public void setBankImage(String bankImage) {
		this.bankImage = bankImage;
	}

}