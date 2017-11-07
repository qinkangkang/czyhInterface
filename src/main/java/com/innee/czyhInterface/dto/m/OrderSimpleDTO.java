package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class OrderSimpleDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventImageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String specTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String session;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;

	private BigDecimal price = BigDecimal.ZERO;

	private int count = 0;

	private BigDecimal postage = BigDecimal.ZERO;

	private BigDecimal changeAmount = BigDecimal.ZERO;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String changeAmountInstruction;

	private BigDecimal total = BigDecimal.ZERO;

	private int status = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;

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

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}

	public String getChangeAmountInstruction() {
		return changeAmountInstruction;
	}

	public void setChangeAmountInstruction(String changeAmountInstruction) {
		this.changeAmountInstruction = changeAmountInstruction;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public String getEventImageUrl() {
		return eventImageUrl;
	}

	public void setEventImageUrl(String eventImageUrl) {
		this.eventImageUrl = eventImageUrl;
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}

	public String getSpecTitle() {
		return specTitle;
	}

	public void setSpecTitle(String specTitle) {
		this.specTitle = specTitle;
	}

}