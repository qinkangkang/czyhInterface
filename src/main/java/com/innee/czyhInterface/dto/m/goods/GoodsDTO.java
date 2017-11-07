package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class GoodsDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String bargainingId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String type;

	private int categoryId = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String originalPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String presentPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String channelId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String desc;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;

	private int stockFlag = 0;

	private int status = 0;

	private int sellModel = 0;

	private int specModel = 0;

	private int promotionModel = 0;

	private int limitation = 0;

	// 已售百分比
	private int percentage = 0;

	// 已售数量
	private Integer saleTotal = 0;

	private int sdealsModel = 0;

	private boolean useCoupon = true;

	private boolean ifInStock = false;

	private int count = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String distance;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String createTime;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getPresentPrice() {
		return presentPrice;
	}

	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public int getStockFlag() {
		return stockFlag;
	}

	public void setStockFlag(int stockFlag) {
		this.stockFlag = stockFlag;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
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

	public int getSellModel() {
		return sellModel;
	}

	public void setSellModel(int sellModel) {
		this.sellModel = sellModel;
	}

	public int getSpecModel() {
		return specModel;
	}

	public void setSpecModel(int specModel) {
		this.specModel = specModel;
	}

	public int getPromotionModel() {
		return promotionModel;
	}

	public void setPromotionModel(int promotionModel) {
		this.promotionModel = promotionModel;
	}

	public int getLimitation() {
		return limitation;
	}

	public void setLimitation(int limitation) {
		this.limitation = limitation;
	}

	public boolean isIfInStock() {
		return ifInStock;
	}

	public void setIfInStock(boolean ifInStock) {
		this.ifInStock = ifInStock;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getSdealsModel() {
		return sdealsModel;
	}

	public void setSdealsModel(int sdealsModel) {
		this.sdealsModel = sdealsModel;
	}

	public boolean isUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(boolean useCoupon) {
		this.useCoupon = useCoupon;
	}

	public int getPercentage() {
		return percentage;
	}

	public void setPercentage(int percentage) {
		this.percentage = percentage;
	}

	public Integer getSaleTotal() {
		return saleTotal;
	}

	public void setSaleTotal(Integer saleTotal) {
		this.saleTotal = saleTotal;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getBargainingId() {
		return bargainingId;
	}

	public void setBargainingId(String bargainingId) {
		this.bargainingId = bargainingId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

}