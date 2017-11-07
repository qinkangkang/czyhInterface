package com.innee.czyhInterface.util.wx;

import java.io.Serializable;

public class WxPayResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success = false;

	private String appId;

	private String partnerId;

	private String prepayId;

	private String paySign;

	private String payPackage;

	private String timestamp;

	private String signType;

	private String nonceStrVal;

	private String request;

	private String response;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getPrepayId() {
		return prepayId;
	}

	public void setPrepayId(String prepayId) {
		this.prepayId = prepayId;
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

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getNonceStrVal() {
		return nonceStrVal;
	}

	public void setNonceStrVal(String nonceStrVal) {
		this.nonceStrVal = nonceStrVal;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

}