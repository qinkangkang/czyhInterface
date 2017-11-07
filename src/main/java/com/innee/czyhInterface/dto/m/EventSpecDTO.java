package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class EventSpecDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String specId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sessionId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String title;

	private BigDecimal price = BigDecimal.ZERO;

	private BigDecimal deal = BigDecimal.ZERO;

	private BigDecimal postage = BigDecimal.ZERO;

	private int total = 0;

	private int stock = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String stockUnit;

	public String getSpecId() {
		return specId;
	}

	public void setSpecId(String specId) {
		this.specId = specId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getDeal() {
		return deal;
	}

	public void setDeal(BigDecimal deal) {
		this.deal = deal;
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getStockUnit() {
		return stockUnit;
	}

	public void setStockUnit(String stockUnit) {
		this.stockUnit = stockUnit;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}