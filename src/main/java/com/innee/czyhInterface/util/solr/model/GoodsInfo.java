/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.innee.czyhInterface.util.solr.model;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 商品solr实体类
 * 
 * @author jinsey
 *
 */
public class GoodsInfo implements SearchableGoodsInfo {

	@Field(ID_FIELD)
	private String id;

	@Field(BARGAININGID_FIELD)
	private String bargainingId;
	
	@Field(NAME_FIELD)
	private String name;

	@Field(IMAGE_FIELD)
	private String imageUrl;

	@Field(TYPE_FIELD)
	private String type;

	@Field(SPEC_FIELD)
	private String spec;

	@Field(ORIGINALPRICE_FIELD)
	private String originalPrice;

	@Field(PRESENTPRICE_FIELD)
	private String presentPrice;

	@Field(DESC_FIELD)
	private String desc;

	@Field(SPONSORNAME_FIELD)
	private String sponsorName;

	@Field(STOCKFLAG_FIELD)
	private Integer stockFlag;

	@Field(STATUS_FIELD)
	private Integer status;

	@Field(SELLMODEL_FIELD)
	private Integer sellModel;

	@Field(SPECMODEL_FIELD)
	private Integer specModel;

	@Field(PROMOTIONMODEL_FIELD)
	private Integer promotionModel;

	@Field(LIMITATION_FIELD)
	private Integer limitation;

	@Field(PERCENTAGE_FIELD)
	private Integer percentage;

	@Field(SALETOTAL_FIELD)
	private Integer saleTotal;

	@Field(SDEALSMODEL_FIELD)
	private Integer sdealsModel;

	@Field(USECOUPON_FIELD)
	private boolean useCoupon;

	@Field(IFINSTOCK_FIELD)
	private boolean ifInStock;

	@Field(COUNT_FIELD)
	private Integer count;

	@Field(STATUSSTRING_FIELD)
	private String statusString;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the imageUrl
	 */
	public String getImageUrl() {
		return imageUrl;
	}

	/**
	 * @param imageUrl
	 *            the imageUrl to set
	 */
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the spec
	 */
	public String getSpec() {
		return spec;
	}

	/**
	 * @param spec
	 *            the spec to set
	 */
	public void setSpec(String spec) {
		this.spec = spec;
	}

	/**
	 * @return the originalPrice
	 */
	public String getOriginalPrice() {
		return originalPrice;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param originalPrice
	 *            the originalPrice to set
	 */
	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	/**
	 * @return the presentPrice
	 */
	public String getPresentPrice() {
		return presentPrice;
	}

	/**
	 * @param presentPrice
	 *            the presentPrice to set
	 */
	public void setPresentPrice(String presentPrice) {
		this.presentPrice = presentPrice;
	}

	/**
	 * @return the desc
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param desc
	 *            the desc to set
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the sponsorName
	 */
	public String getSponsorName() {
		return sponsorName;
	}

	/**
	 * @param sponsorName
	 *            the sponsorName to set
	 */
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	/**
	 * @return the stockFlag
	 */
	public Integer getStockFlag() {
		return stockFlag;
	}

	/**
	 * @param stockFlag
	 *            the stockFlag to set
	 */
	public void setStockFlag(Integer stockFlag) {
		this.stockFlag = stockFlag;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the sellModel
	 */
	public Integer getSellModel() {
		return sellModel;
	}

	/**
	 * @param sellModel
	 *            the sellModel to set
	 */
	public void setSellModel(Integer sellModel) {
		this.sellModel = sellModel;
	}

	/**
	 * @return the specModel
	 */
	public Integer getSpecModel() {
		return specModel;
	}

	/**
	 * @param specModel
	 *            the specModel to set
	 */
	public void setSpecModel(Integer specModel) {
		this.specModel = specModel;
	}

	/**
	 * @return the promotionModel
	 */
	public Integer getPromotionModel() {
		return promotionModel;
	}

	/**
	 * @param promotionModel
	 *            the promotionModel to set
	 */
	public void setPromotionModel(Integer promotionModel) {
		this.promotionModel = promotionModel;
	}

	/**
	 * @return the limitation
	 */
	public Integer getLimitation() {
		return limitation;
	}

	/**
	 * @param limitation
	 *            the limitation to set
	 */
	public void setLimitation(Integer limitation) {
		this.limitation = limitation;
	}

	/**
	 * @return the percentage
	 */
	public Integer getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage
	 *            the percentage to set
	 */
	public void setPercentage(Integer percentage) {
		this.percentage = percentage;
	}

	/**
	 * @return the saleTotal
	 */
	public Integer getSaleTotal() {
		return saleTotal;
	}

	/**
	 * @param saleTotal
	 *            the saleTotal to set
	 */
	public void setSaleTotal(Integer saleTotal) {
		this.saleTotal = saleTotal;
	}

	/**
	 * @return the sdealsModel
	 */
	public Integer getSdealsModel() {
		return sdealsModel;
	}

	/**
	 * @param sdealsModel
	 *            the sdealsModel to set
	 */
	public void setSdealsModel(Integer sdealsModel) {
		this.sdealsModel = sdealsModel;
	}

	/**
	 * @return the useCoupon
	 */
	public boolean isUseCoupon() {
		return useCoupon;
	}

	/**
	 * @param useCoupon
	 *            the useCoupon to set
	 */
	public void setUseCoupon(boolean useCoupon) {
		this.useCoupon = useCoupon;
	}

	/**
	 * @return the ifInStock
	 */
	public boolean isIfInStock() {
		return ifInStock;
	}

	/**
	 * @param ifInStock
	 *            the ifInStock to set
	 */
	public void setIfInStock(boolean ifInStock) {
		this.ifInStock = ifInStock;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count
	 *            the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * @return the statusString
	 */
	public String getStatusString() {
		return statusString;
	}

	/**
	 * @param statusString
	 *            the statusString to set
	 */
	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "GoodsInfo [id=" + id + ", name=" + name + ", imageUrl=" + imageUrl + ", type=" + type + ", spec=" + spec
				+ ", originalPrice=" + originalPrice + ", presentPrice=" + presentPrice + ", desc=" + desc
				+ ", sponsorName=" + sponsorName + ", stockFlag=" + stockFlag + ", status=" + status + ", sellModel="
				+ sellModel + ", specModel=" + specModel + ", promotionModel=" + promotionModel + ", limitation="
				+ limitation + ", percentage=" + percentage + ", saleTotal=" + saleTotal + ", sdealsModel="
				+ sdealsModel + ", useCoupon=" + useCoupon + ", ifInStock=" + ifInStock + ", count=" + count
				+ ", statusString=" + statusString + "]";
	}

}
