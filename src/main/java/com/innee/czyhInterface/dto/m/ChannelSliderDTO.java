package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ChannelSliderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String sliderImageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String linkUrl;

	/**
	 * 活动ID或者商家ID或者专题ID
	 */
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String entityId;
	
	/**
	 * 文字
	 */
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String title;
	
	/**
	 * 轮播链接类型，主要是给APP来识别跳转到不同页面的标示。 1表示是活动链接；2表示是商家链接；3表示是专题链接，4表示优惠券
	 */
	private int urlType = 0;

	public String getSliderImageUrl() {
		return sliderImageUrl;
	}

	public void setSliderImageUrl(String sliderImageUrl) {
		this.sliderImageUrl = sliderImageUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public int getUrlType() {
		return urlType;
	}

	public void setUrlType(int urlType) {
		this.urlType = urlType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}