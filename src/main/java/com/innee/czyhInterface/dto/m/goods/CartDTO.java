package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.CartGoodsDTO;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class CartDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<CartGoodsDTO> cartGoodsDTOs;
	
	private BigDecimal changeAmount = BigDecimal.ZERO;

	private BigDecimal receivableTotal = BigDecimal.ZERO;

	private BigDecimal total = BigDecimal.ZERO;

	private BigDecimal freight = BigDecimal.ZERO;
	
	private BigDecimal goodsTotal = BigDecimal.ZERO;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String freePostage;

	public List<CartGoodsDTO> getCartGoodsDTOs() {
		return cartGoodsDTOs;
	}

	public void setCartGoodsDTOs(List<CartGoodsDTO> cartGoodsDTOs) {
		this.cartGoodsDTOs = cartGoodsDTOs;
	}

	public BigDecimal getChangeAmount() {
		return changeAmount;
	}

	public void setChangeAmount(BigDecimal changeAmount) {
		this.changeAmount = changeAmount;
	}

	public BigDecimal getReceivableTotal() {
		return receivableTotal;
	}

	public void setReceivableTotal(BigDecimal receivableTotal) {
		this.receivableTotal = receivableTotal;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(BigDecimal freight) {
		this.freight = freight;
	}

	public BigDecimal getGoodsTotal() {
		return goodsTotal;
	}

	public void setGoodsTotal(BigDecimal goodsTotal) {
		this.goodsTotal = goodsTotal;
	}

	public String getFreePostage() {
		return freePostage;
	}

	public void setFreePostage(String freePostage) {
		this.freePostage = freePostage;
	}
	
}