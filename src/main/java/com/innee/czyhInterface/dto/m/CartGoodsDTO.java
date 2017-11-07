package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CartGoodsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsId;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsSkuId;
	
	private boolean checked = false;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String presentPrice;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String originalPrice;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String promotionModel;
	
	private int limitation = -1;

	private int count = 0;
	
	private int sdealsModel = 0;

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

	public String getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getPromotionModel() {
		return promotionModel;
	}

	public void setPromotionModel(String promotionModel) {
		this.promotionModel = promotionModel;
	}

	public int getLimitation() {
		return limitation;
	}

	public void setLimitation(int limitation) {
		this.limitation = limitation;
	}

	public String getGoodsSkuId() {
		return goodsSkuId;
	}

	public void setGoodsSkuId(String goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getSdealsModel() {
		return sdealsModel;
	}

	public void setSdealsModel(int sdealsModel) {
		this.sdealsModel = sdealsModel;
	}
	
}