package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class GoodsDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String[] imageUrls;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String type;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsSubTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String originalPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String presentPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String pinkage;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String spec;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String desc;

	private int status = 0;

	// 剩余库存
	private Integer stock = 0;

	// 已售数量
	private Integer saleTotal = 0;

	private boolean useCoupon = true;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String detailHtmlUrl;

	private int sellModel = 0;

	private int specModel = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String promotionModel;

	private int limitation = -1;

	private boolean ifInStock = true;

	private boolean favorite = false;

	private int count = 0;

	private boolean ifSeckill = false;

	private int allComment = 0;

	private int goodComment = 0;

	private int badComment = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String rating;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private List<String> couponString;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private List<String> goodsLabel;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValue1Name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValue1Id;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValue2Name;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsValue2Id;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsSkuId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsSpecImage;

	private boolean canChoose = false;

	private boolean singleSku = false;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String[] getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public String getGoodsTime() {
		return goodsTime;
	}

	public void setGoodsTime(String goodsTime) {
		this.goodsTime = goodsTime;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
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

	public boolean isIfInStock() {
		return ifInStock;
	}

	public void setIfInStock(boolean ifInStock) {
		this.ifInStock = ifInStock;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getDetailHtmlUrl() {
		return detailHtmlUrl;
	}

	public void setDetailHtmlUrl(String detailHtmlUrl) {
		this.detailHtmlUrl = detailHtmlUrl;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
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

	public String getPinkage() {
		return pinkage;
	}

	public void setPinkage(String pinkage) {
		this.pinkage = pinkage;
	}

	public boolean isIfSeckill() {
		return ifSeckill;
	}

	public void setIfSeckill(boolean ifSeckill) {
		this.ifSeckill = ifSeckill;
	}

	public boolean isUseCoupon() {
		return useCoupon;
	}

	public void setUseCoupon(boolean useCoupon) {
		this.useCoupon = useCoupon;
	}

	public String getGoodsSubTitle() {
		return goodsSubTitle;
	}

	public void setGoodsSubTitle(String goodsSubTitle) {
		this.goodsSubTitle = goodsSubTitle;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Integer getSaleTotal() {
		return saleTotal;
	}

	public void setSaleTotal(Integer saleTotal) {
		this.saleTotal = saleTotal;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getAllComment() {
		return allComment;
	}

	public void setAllComment(int allComment) {
		this.allComment = allComment;
	}

	public int getGoodComment() {
		return goodComment;
	}

	public void setGoodComment(int goodComment) {
		this.goodComment = goodComment;
	}

	public int getBadComment() {
		return badComment;
	}

	public void setBadComment(int badComment) {
		this.badComment = badComment;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public List<String> getCouponString() {
		return couponString;
	}

	public void setCouponString(List<String> couponString) {
		this.couponString = couponString;
	}

	public List<String> getGoodsLabel() {
		return goodsLabel;
	}

	public void setGoodsLabel(List<String> goodsLabel) {
		this.goodsLabel = goodsLabel;
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

	public boolean isCanChoose() {
		return canChoose;
	}

	public void setCanChoose(boolean canChoose) {
		this.canChoose = canChoose;
	}

	public boolean isSingleSku() {
		return singleSku;
	}

	public void setSingleSku(boolean singleSku) {
		this.singleSku = singleSku;
	}

	public String getGoodsSkuId() {
		return goodsSkuId;
	}

	public void setGoodsSkuId(String goodsSkuId) {
		this.goodsSkuId = goodsSkuId;
	}

	public String getGoodsSpecImage() {
		return goodsSpecImage;
	}

	public void setGoodsSpecImage(String goodsSpecImage) {
		this.goodsSpecImage = goodsSpecImage;
	}

}