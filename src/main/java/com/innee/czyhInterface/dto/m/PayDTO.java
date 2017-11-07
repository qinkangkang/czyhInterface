package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class PayDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String partnerId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String prepayId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String paySign;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String payPackage;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String nonceStr;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String timestamp;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String appId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String signType;

	private int payType = 0;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public String getPaySign() {
		return paySign;
	}

	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}

	public String getPayPackage() {
		return payPackage;
	}

	public void setPayPackage(String payPackage) {
		this.payPackage = payPackage;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

}