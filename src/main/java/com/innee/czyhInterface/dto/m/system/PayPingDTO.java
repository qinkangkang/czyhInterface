package com.innee.czyhInterface.dto.m.system;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class PayPingDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String id;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String object;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String created;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String livemode;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String paid;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String refunded;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String app;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String channel;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNo;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String clientIp;

	private int amount = 0;

	private int amountSettle = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String currency;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String subject;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String body;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String timePaid;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String timeExpire;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String timeEettle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String transactionNo;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String pingUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public String getLivemode() {
		return livemode;
	}

	public void setLivemode(String livemode) {
		this.livemode = livemode;
	}

	public String getPaid() {
		return paid;
	}

	public void setPaid(String paid) {
		this.paid = paid;
	}

	public String getRefunded() {
		return refunded;
	}

	public void setRefunded(String refunded) {
		this.refunded = refunded;
	}

	public String getApp() {
		return app;
	}

	public void setApp(String app) {
		this.app = app;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getAmountSettle() {
		return amountSettle;
	}

	public void setAmountSettle(int amountSettle) {
		this.amountSettle = amountSettle;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
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

	public String getTimePaid() {
		return timePaid;
	}

	public void setTimePaid(String timePaid) {
		this.timePaid = timePaid;
	}

	public String getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}

	public String getTimeEettle() {
		return timeEettle;
	}

	public void setTimeEettle(String timeEettle) {
		this.timeEettle = timeEettle;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public String getPingUrl() {
		return pingUrl;
	}

	public void setPingUrl(String pingUrl) {
		this.pingUrl = pingUrl;
	}

}