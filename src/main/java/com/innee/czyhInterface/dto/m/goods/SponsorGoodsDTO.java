package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class SponsorGoodsDTO implements Serializable {

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
	private String goodsTime;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String desc;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String originalPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String presentPrice;

	
	private Integer saleTotal=0;
	
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

	public String getGoodsTime() {
		return goodsTime;
	}

	public void setGoodsTime(String goodsTime) {
		this.goodsTime = goodsTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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

	public Integer getSaleTotal() {
		return saleTotal;
	}

	public void setSaleTotal(Integer saleTotal) {
		this.saleTotal = saleTotal;
	}
	

}