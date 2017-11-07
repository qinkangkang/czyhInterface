package com.innee.czyhInterface.dto.m.goods;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class OtherDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private int tagId = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String goodsTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String channelId;

	public String getGoodsTime() {
		return goodsTime;
	}

	public void setGoodsTime(String goodsTime) {
		this.goodsTime = goodsTime;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public int getTagId() {
		return tagId;
	}

	public void setTagId(int tagId) {
		this.tagId = tagId;
	}

}