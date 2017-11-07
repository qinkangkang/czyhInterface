package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.CartGoodsDTO;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class GoodsSkuValueDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String skuValueId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String skuValueName;

	public String getSkuValueId() {
		return skuValueId;
	}

	public void setSkuValueId(String skuValueId) {
		this.skuValueId = skuValueId;
	}

	public String getSkuValueName() {
		return skuValueName;
	}

	public void setSkuValueName(String skuValueName) {
		this.skuValueName = skuValueName;
	}
	
	
}