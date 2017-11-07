package com.innee.czyhInterface.dto.m.order;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class OrderGoodsDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsName;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsSub;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsSubTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsImage;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;

	private BigDecimal price = BigDecimal.ZERO;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String countPrice;

	private int goodsNum = 0;
	
	private int sdealsModel = 0;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getCountPrice() {
		return countPrice;
	}

	public void setCountPrice(String countPrice) {
		this.countPrice = countPrice;
	}

	public String getGoodsSub() {
		return goodsSub;
	}

	public void setGoodsSub(String goodsSub) {
		this.goodsSub = goodsSub;
	}

	public String getGoodsSubTitle() {
		return goodsSubTitle;
	}

	public void setGoodsSubTitle(String goodsSubTitle) {
		this.goodsSubTitle = goodsSubTitle;
	}

	public int getSdealsModel() {
		return sdealsModel;
	}

	public void setSdealsModel(int sdealsModel) {
		this.sdealsModel = sdealsModel;
	}

}