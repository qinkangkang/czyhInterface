package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class MerchantSimpleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantLogoUrl;

	private BigDecimal score = BigDecimal.ZERO;

	private long eventCount = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;

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

	public long getEventCount() {
		return eventCount;
	}

	public void setEventCount(long eventCount) {
		this.eventCount = eventCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

}