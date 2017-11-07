package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class SpikeGoodsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String originalPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String presentPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String desc;

	private int status = 0;
	
	private int giftModel = 0;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String  giftImageUrl;
	
	private boolean ifInStock = false;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getGoodsTitle() {
		return goodsTitle;
	}

	public void setGoodsTitle(String goodsTitle) {
		this.goodsTitle = goodsTitle;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getGiftModel() {
		return giftModel;
	}

	public void setGiftModel(int giftModel) {
		this.giftModel = giftModel;
	}

	public boolean isIfInStock() {
		return ifInStock;
	}

	public void setIfInStock(boolean ifInStock) {
		this.ifInStock = ifInStock;
	}

	public String getGiftImageUrl() {
		return giftImageUrl;
	}

	public void setGiftImageUrl(String giftImageUrl) {
		this.giftImageUrl = giftImageUrl;
	}

}