package com.innee.czyhInterface.dto.b;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class BSponsorDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorImage;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorPerson;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorPhone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorAddress;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String loginPhone;

	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getSponsorImage() {
		return sponsorImage;
	}

	public void setSponsorImage(String sponsorImage) {
		this.sponsorImage = sponsorImage;
	}

	public String getSponsorPerson() {
		return sponsorPerson;
	}

	public void setSponsorPerson(String sponsorPerson) {
		this.sponsorPerson = sponsorPerson;
	}

	public String getSponsorPhone() {
		return sponsorPhone;
	}

	public void setSponsorPhone(String sponsorPhone) {
		this.sponsorPhone = sponsorPhone;
	}

	public String getSponsorAddress() {
		return sponsorAddress;
	}

	public void setSponsorAddress(String sponsorAddress) {
		this.sponsorAddress = sponsorAddress;
	}

	public String getLoginPhone() {
		return loginPhone;
	}

	public void setLoginPhone(String loginPhone) {
		this.loginPhone = loginPhone;
	}

}