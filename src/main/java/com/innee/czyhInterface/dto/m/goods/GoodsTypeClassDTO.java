package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class GoodsTypeClassDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsSkuId;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String typeValueName;

	private int limitation = -1;

	private int fstock = 0;

//	private BigDecimal price = BigDecimal.ZERO;

	private BigDecimal priceMoney = BigDecimal.ZERO;

	public String getTypeValueName() {
		return typeValueName;
	}

	public void setTypeValueName(String typeValueName) {
		this.typeValueName = typeValueName;
	}

	public int getFstock() {
		return fstock;
	}

	public void setFstock(int fstock) {
		this.fstock = fstock;
	}

	public BigDecimal getPriceMoney() {
		return priceMoney;
	}

	public void setPriceMoney(BigDecimal priceMoney) {
		this.priceMoney = priceMoney;
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
	
}