package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ChannelDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String channelId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String title;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String code;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String subTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String promotion;

	private int defaultOrderType = 0;

	private boolean slider = false;

	private List<ChannelSliderDTO> sliderList;

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getPromotion() {
		return promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	public int getDefaultOrderType() {
		return defaultOrderType;
	}

	public void setDefaultOrderType(int defaultOrderType) {
		this.defaultOrderType = defaultOrderType;
	}

	public boolean isSlider() {
		return slider;
	}

	public void setSlider(boolean slider) {
		this.slider = slider;
	}

	public List<ChannelSliderDTO> getSliderList() {
		return sliderList;
	}

	public void setSliderList(List<ChannelSliderDTO> sliderList) {
		this.sliderList = sliderList;
	}

}