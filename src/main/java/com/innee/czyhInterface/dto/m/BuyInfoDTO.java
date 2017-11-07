package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class BuyInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String specId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String specTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String couponDeliveryId;

	private BigDecimal amount = BigDecimal.ZERO;

	private BigDecimal price = BigDecimal.ZERO;

	private BigDecimal deal = BigDecimal.ZERO;

	private int count = 0;

	private int orderType = 0;

	private int appointment = 0;

	private int returnReplace = 0;

	private int usePreferential = 0;

	private int specPerson = 0;

	private BigDecimal postage = BigDecimal.ZERO;

	private BigDecimal receivableTotal = BigDecimal.ZERO;

	private BigDecimal total = BigDecimal.ZERO;
	
	private List eventExtInfos;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String recipient;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String phone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Integer ifNeedPhone = 0;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionTitle() {
		return sessionTitle;
	}

	public void setSessionTitle(String sessionTitle) {
		this.sessionTitle = sessionTitle;
	}

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public String getSpecTitle() {
		return specTitle;
	}

	public void setSpecTitle(String specTitle) {
		this.specTitle = specTitle;
	}

	public String getCouponDeliveryId() {
		return couponDeliveryId;
	}

	public void setCouponDeliveryId(String couponDeliveryId) {
		this.couponDeliveryId = couponDeliveryId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getSpecPerson() {
		return specPerson;
	}

	public void setSpecPerson(int specPerson) {
		this.specPerson = specPerson;
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public BigDecimal getReceivableTotal() {
		return receivableTotal;
	}

	public void setReceivableTotal(BigDecimal receivableTotal) {
		this.receivableTotal = receivableTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getDeal() {
		return deal;
	}

	public void setDeal(BigDecimal deal) {
		this.deal = deal;
	}

	public int getAppointment() {
		return appointment;
	}

	public void setAppointment(int appointment) {
		this.appointment = appointment;
	}

	public int getReturnReplace() {
		return returnReplace;
	}

	public void setReturnReplace(int returnReplace) {
		this.returnReplace = returnReplace;
	}

	public int getUsePreferential() {
		return usePreferential;
	}

	public void setUsePreferential(int usePreferential) {
		this.usePreferential = usePreferential;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List getEventExtInfos() {
		return eventExtInfos;
	}

	public void setEventExtInfos(List eventExtInfos) {
		this.eventExtInfos = eventExtInfos;
	}

	public Integer getIfNeedPhone() {
		return ifNeedPhone;
	}

	public void setIfNeedPhone(Integer ifNeedPhone) {
		this.ifNeedPhone = ifNeedPhone;
	}


}