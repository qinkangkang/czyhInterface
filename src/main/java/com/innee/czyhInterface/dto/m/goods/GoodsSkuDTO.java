package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class GoodsSkuDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValue1Name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValue1Id;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String skuId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValue2Name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValue2Id;

	private BigDecimal priceMoney = BigDecimal.ZERO;

	private int stock = 0;

	private int limitation = -1;

	// private List<GoodsTypeClassDTO> goodsTypeClassList =
	// Lists.newArrayList();

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public int getLimitation() {
		return limitation;
	}

	public void setLimitation(int limitation) {
		this.limitation = limitation;
	}

	public BigDecimal getPriceMoney() {
		return priceMoney;
	}

	public void setPriceMoney(BigDecimal priceMoney) {
		this.priceMoney = priceMoney;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getGoodsValue1Name() {
		return goodsValue1Name;
	}

	public void setGoodsValue1Name(String goodsValue1Name) {
		this.goodsValue1Name = goodsValue1Name;
	}

	public String getGoodsValue1Id() {
		return goodsValue1Id;
	}

	public void setGoodsValue1Id(String goodsValue1Id) {
		this.goodsValue1Id = goodsValue1Id;
	}

	public String getGoodsValue2Name() {
		return goodsValue2Name;
	}

	public void setGoodsValue2Name(String goodsValue2Name) {
		this.goodsValue2Name = goodsValue2Name;
	}

	public String getGoodsValue2Id() {
		return goodsValue2Id;
	}

	public void setGoodsValue2Id(String goodsValue2Id) {
		this.goodsValue2Id = goodsValue2Id;
	}
	
}