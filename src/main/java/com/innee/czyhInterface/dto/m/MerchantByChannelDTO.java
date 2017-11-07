package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class MerchantByChannelDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantLogoUrl;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String linkUrl;
	
	private BigDecimal score = BigDecimal.ZERO;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;

	private List<EventByChannelDTO> eventList;

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantLogoUrl() {
		return merchantLogoUrl;
	}

	public void setMerchantLogoUrl(String merchantLogoUrl) {
		this.merchantLogoUrl = merchantLogoUrl;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<EventByChannelDTO> getEventList() {
		return eventList;
	}

	public void setEventList(List<EventByChannelDTO> eventList) {
		this.eventList = eventList;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

}