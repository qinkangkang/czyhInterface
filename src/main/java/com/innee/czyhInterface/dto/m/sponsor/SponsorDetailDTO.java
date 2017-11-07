package com.innee.czyhInterface.dto.m.sponsor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class SponsorDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorBrief;

	private String[] sponsorImage = new String[] {};

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorIcon;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String score;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String tag;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String warmPrompt;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String validityDate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String exceptDate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String useDate;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String perPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String phone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String region;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gps;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String secondKill = "0";

	private boolean favorite = false;

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

	public String[] getSponsorImage() {
		return sponsorImage;
	}

	public void setSponsorImage(String[] sponsorImage) {
		this.sponsorImage = sponsorImage;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPerPrice() {
		return perPrice;
	}

	public void setPerPrice(String perPrice) {
		this.perPrice = perPrice;
	}

	public String getWarmPrompt() {
		return warmPrompt;
	}

	public void setWarmPrompt(String warmPrompt) {
		this.warmPrompt = warmPrompt;
	}

	public String getValidityDate() {
		return validityDate;
	}

	public void setValidityDate(String validityDate) {
		this.validityDate = validityDate;
	}

	public String getExceptDate() {
		return exceptDate;
	}

	public void setExceptDate(String exceptDate) {
		this.exceptDate = exceptDate;
	}

	public String getUseDate() {
		return useDate;
	}

	public void setUseDate(String useDate) {
		this.useDate = useDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public String getSponsorIcon() {
		return sponsorIcon;
	}

	public void setSponsorIcon(String sponsorIcon) {
		this.sponsorIcon = sponsorIcon;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSecondKill() {
		return secondKill;
	}

	public void setSecondKill(String secondKill) {
		this.secondKill = secondKill;
	}

	public String getSponsorBrief() {
		return sponsorBrief;
	}

	public void setSponsorBrief(String sponsorBrief) {
		this.sponsorBrief = sponsorBrief;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

}