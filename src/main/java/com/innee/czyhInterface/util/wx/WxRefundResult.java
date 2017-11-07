package com.innee.czyhInterface.util.wx;

import java.io.Serializable;

public class WxRefundResult implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean success = false;

	private String refundId;

	private int refundFee;

	private int cashRefundFee;

	private int couponRefundFee;

	private int couponRefundCount;

	private String request;

	private String response;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public int getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(int refundFee) {
		this.refundFee = refundFee;
	}

	public int getCashRefundFee() {
		return cashRefundFee;
	}

	public void setCashRefundFee(int cashRefundFee) {
		this.cashRefundFee = cashRefundFee;
	}

	public int getCouponRefundFee() {
		return couponRefundFee;
	}

	public void setCouponRefundFee(int couponRefundFee) {
		this.couponRefundFee = couponRefundFee;
	}

	public int getCouponRefundCount() {
		return couponRefundCount;
	}

	public void setCouponRefundCount(int couponRefundCount) {
		this.couponRefundCount = couponRefundCount;
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

}