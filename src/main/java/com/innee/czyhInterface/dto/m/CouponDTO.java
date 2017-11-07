package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CouponDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String couponDeliveryId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String title;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String couponId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String useStartTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String useEndTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String useRange;

	private BigDecimal amount;

	private BigDecimal discount;

	private BigDecimal limitation;

	private Integer status;

	/** 默认优惠券没领取光 */
	private boolean isReceiveFinish = false;

	/** 默认用户没领取 */
	private boolean isReceive = false;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String limitationInfo;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String limitationClient;
	// private int useType = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String limitationEvent;

	public String getCouponDeliveryId() {
		return couponDeliveryId;
	}

	public void setCouponDeliveryId(String couponDeliveryId) {
		this.couponDeliveryId = couponDeliveryId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCouponId() {
		return couponId;
	}

	public void setCouponId(String couponId) {
		this.couponId = couponId;
	}

	public String getUseStartTime() {
		return useStartTime;
	}

	public void setUseStartTime(String useStartTime) {
		this.useStartTime = useStartTime;
	}

	public String getUseEndTime() {
		return useEndTime;
	}

	public void setUseEndTime(String useEndTime) {
		this.useEndTime = useEndTime;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getLimitation() {
		return limitation;
	}

	public void setLimitation(BigDecimal limitation) {
		this.limitation = limitation;
	}

	public String getLimitationInfo() {
		return limitationInfo;
	}

	public void setLimitationInfo(String limitationInfo) {
		this.limitationInfo = limitationInfo;
	}

	public String getLimitationClient() {
		return limitationClient;
	}

	public void setLimitationClient(String limitationClient) {
		this.limitationClient = limitationClient;
	}

	/*
	 * public int getUseType() { return useType; }
	 * 
	 * public void setUseType(int useType) { this.useType = useType; }
	 */

	public Integer getStatus() {
		return status;
	}

	public boolean isReceiveFinish() {
		return isReceiveFinish;
	}

	public void setReceiveFinish(boolean isReceiveFinish) {
		this.isReceiveFinish = isReceiveFinish;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getLimitationEvent() {
		return limitationEvent;
	}

	public String getUseRange() {
		return useRange;
	}

	public void setUseRange(String useRange) {
		this.useRange = useRange;
	}

	public boolean isReceive() {
		return isReceive;
	}

	public void setReceive(boolean isReceive) {
		this.isReceive = isReceive;
	}

	public void setLimitationEvent(String limitationEvent) {
		this.limitationEvent = limitationEvent;
	}

}
