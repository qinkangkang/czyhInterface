package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.order.OrderGoodsDTO;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String orderNum;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsSubTitle;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsPrice;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorPhone;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String[] goodsImageUrl;

	private int orderType = 0;

	private BigDecimal total = BigDecimal.ZERO;
	
	private BigDecimal refundAmount = BigDecimal.ZERO;

	private int payType = 0;

	private int sellModel = 0;

	private int promotionModel = 0;

	private int totalNum = 0;

	private int status = 0;
	
	private int sponsorModel = 0;

	private BigDecimal postage = BigDecimal.ZERO;

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

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
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

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getPayType() {
		return payType;
	}

	public void setPayType(int payType) {
		this.payType = payType;
	}

	public int getSellModel() {
		return sellModel;
	}

	public void setSellModel(int sellModel) {
		this.sellModel = sellModel;
	}

	public int getPromotionModel() {
		return promotionModel;
	}

	public void setPromotionModel(int promotionModel) {
		this.promotionModel = promotionModel;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
	}

	public String[] getGoodsImageUrl() {
		return goodsImageUrl;
	}

	public void setGoodsImageUrl(String[] goodsImageUrl) {
		this.goodsImageUrl = goodsImageUrl;
	}

	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public BigDecimal getPostage() {
		return postage;
	}

	public void setPostage(BigDecimal postage) {
		this.postage = postage;
	}

	public String getGoodsSubTitle() {
		return goodsSubTitle;
	}

	public void setGoodsSubTitle(String goodsSubTitle) {
		this.goodsSubTitle = goodsSubTitle;
	}

	public String getSponsorPhone() {
		return sponsorPhone;
	}

	public void setSponsorPhone(String sponsorPhone) {
		this.sponsorPhone = sponsorPhone;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getSponsorModel() {
		return sponsorModel;
	}

	public void setSponsorModel(int sponsorModel) {
		this.sponsorModel = sponsorModel;
	}

}