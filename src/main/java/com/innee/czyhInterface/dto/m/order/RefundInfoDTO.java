package com.innee.czyhInterface.dto.m.order;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class RefundInfoDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String refundDesc;

	private List<RefundReasonDTO> refundReason = Lists.newArrayList();
	
	private Integer checkRefundReason = 0;

	private String refundPrice = "0";
	
	private String checkRefundPrice = "0";
	
	private String postage = "0";
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsStatus;

	private List<OrderGoodsDetailDTO> orderGoodsList = Lists.newArrayList();

	public String getRefundDesc() {
		return refundDesc;
	}

	public void setRefundDesc(String refundDesc) {
		this.refundDesc = refundDesc;
	}

	public String getRefundPrice() {
		return refundPrice;
	}

	public void setRefundPrice(String refundPrice) {
		this.refundPrice = refundPrice;
	}

	public String getPostage() {
		return postage;
	}

	public void setPostage(String postage) {
		this.postage = postage;
	}

	public List<OrderGoodsDetailDTO> getOrderGoodsList() {
		return orderGoodsList;
	}

	public void setOrderGoodsList(List<OrderGoodsDetailDTO> orderGoodsList) {
		this.orderGoodsList = orderGoodsList;
	}

	public Integer getCheckRefundReason() {
		return checkRefundReason;
	}

	public void setCheckRefundReason(Integer checkRefundReason) {
		this.checkRefundReason = checkRefundReason;
	}

	public List<RefundReasonDTO> getRefundReason() {
		return refundReason;
	}

	public void setRefundReason(List<RefundReasonDTO> refundReason) {
		this.refundReason = refundReason;
	}

	public String getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(String goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public String getCheckRefundPrice() {
		return checkRefundPrice;
	}

	public void setCheckRefundPrice(String checkRefundPrice) {
		this.checkRefundPrice = checkRefundPrice;
	}

}