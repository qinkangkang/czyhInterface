package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class GoodsValueDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValueName1;

	List<GoodsSkuValueDTO> goodsValue1 = Lists.newArrayList();
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValueName2;
	
	List<GoodsSkuValueDTO> goodsValue2 = Lists.newArrayList();

	public String getGoodsValueName1() {
		return goodsValueName1;
	}

	public void setGoodsValueName1(String goodsValueName1) {
		this.goodsValueName1 = goodsValueName1;
	}

	public List<GoodsSkuValueDTO> getGoodsValue1() {
		return goodsValue1;
	}

	public void setGoodsValue1(List<GoodsSkuValueDTO> goodsValue1) {
		this.goodsValue1 = goodsValue1;
	}

	public String getGoodsValueName2() {
		return goodsValueName2;
	}

	public void setGoodsValueName2(String goodsValueName2) {
		this.goodsValueName2 = goodsValueName2;
	}

	public List<GoodsSkuValueDTO> getGoodsValue2() {
		return goodsValue2;
	}

	public void setGoodsValue2(List<GoodsSkuValueDTO> goodsValue2) {
		this.goodsValue2 = goodsValue2;
	}

}