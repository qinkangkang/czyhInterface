package com.innee.czyhInterface.dto.m.sponsor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.dto.m.goods.SponsorGoodsDTO;
import com.innee.czyhInterface.entity.TEvent;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class SponsorDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sponsorImage;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String score;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String tag;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String perPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String distance;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String district;
	
	private List<SponsorGoodsDTO> goodsList;
	
	public String getSponsorId() {
		return sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public String getSponsorName() {
		return sponsorName;
	}

	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}

	public String getSponsorImage() {
		return sponsorImage;
	}

	public void setSponsorImage(String sponsorImage) {
		this.sponsorImage = sponsorImage;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPerPrice() {
		return perPrice;
	}

	public void setPerPrice(String perPrice) {
		this.perPrice = perPrice;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public List<SponsorGoodsDTO> getGoodsList() {
		return goodsList;
	}

	public void setGoodsList(List<SponsorGoodsDTO> goodsList) {
		this.goodsList = goodsList;
	}
	

}