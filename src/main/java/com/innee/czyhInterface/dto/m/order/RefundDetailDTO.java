package com.innee.czyhInterface.dto.m.order;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.innee.czyhInterface.dto.m.express.ExpressCompamyDTO;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class RefundDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String refundTime;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String remainingTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String refundReason;

	private String refundPrice = "0";
	
	private Integer refundType = 1;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String payType;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String refundTypeString;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNum;

	private List<OrderGoodsDetailDTO> orderGoodsList = Lists.newArrayList();
	
	private Integer refundStatus = 1;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String expressName;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String expressNum;
	
	private List<ExpressCompamyDTO> expressCompamyList = Lists.newArrayList();

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public String getRemainingTime() {
		return remainingTime;
	}

	public void setRemainingTime(String remainingTime) {
		this.remainingTime = remainingTime;
	}

	public String getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(String refundPrice) {
		this.refundPrice = refundPrice;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public String getRefundTypeString() {
		return refundTypeString;
	}

	public void setRefundTypeString(String refundTypeString) {
		this.refundTypeString = refundTypeString;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public List<OrderGoodsDetailDTO> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(List<OrderGoodsDetailDTO> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public String getExpressName() {
		return expressName;
	}

	public void setExpressName(String expressName) {
		this.expressName = expressName;
	}

	public String getExpressNum() {
		return expressNum;
	}

	public void setExpressNum(String expressNum) {
		this.expressNum = expressNum;
	}

	public List<ExpressCompamyDTO> getExpressCompamyList() {
		return expressCompamyList;
	}

	public void setExpressCompamyList(List<ExpressCompamyDTO> expressCompamyList) {
		this.expressCompamyList = expressCompamyList;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}
	
}