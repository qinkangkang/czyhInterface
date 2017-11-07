package com.innee.czyhInterface.util.pingPay.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ChargeParam implements Serializable {

	private static final long serialVersionUID = 1L;

	private int orderprice = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNo;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String ip;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String channel; // 支付渠道wx20 alipay30

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String subject;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String body;

	private Map<String, Object> extra;

	public int getOrderprice() {
		return orderprice;
	}

	public void setOrderprice(int orderprice) {
		this.orderprice = orderprice;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
