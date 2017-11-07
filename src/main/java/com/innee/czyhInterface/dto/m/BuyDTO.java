package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class BuyDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String originalPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String presentPrice;

	private int stockFlag = 0;

	private int quota = 0;

	private List<EventSessionDTO> sessionList;

	private List<EventSpecDTO> specList;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}

	public List<EventSessionDTO> getSessionList() {
		return sessionList;
	}

	public void setSessionList(List<EventSessionDTO> sessionList) {
		this.sessionList = sessionList;
	}

	public List<EventSpecDTO> getSpecList() {
		return specList;
	}

	public void setSpecList(List<EventSpecDTO> specList) {
		this.specList = specList;
	}

	public int getStockFlag() {
		return stockFlag;
	}

	public void setStockFlag(int stockFlag) {
		this.stockFlag = stockFlag;
	}

	public int getQuota() {
		return quota;
	}

	public void setQuota(int quota) {
		this.quota = quota;
	}

}