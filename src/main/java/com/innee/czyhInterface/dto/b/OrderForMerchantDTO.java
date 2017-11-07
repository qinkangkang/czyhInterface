package com.innee.czyhInterface.dto.b;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class OrderForMerchantDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsTitle;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsImage;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;

	private int count = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String total;

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

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
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

}