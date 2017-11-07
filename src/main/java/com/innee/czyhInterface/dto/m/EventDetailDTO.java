package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class EventDetailDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String[] imageUrls;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String type;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventSubTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventSubTitleImg;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventFocus;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String detailHtmlUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gps;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gpsLng;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gpsLat;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String age;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String originalPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String presentPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventTime;

	private long commentNum = 0L;

	private long subscribeNum = 0L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String distance;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String recommend;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String score;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String siteAndDuration;

	private int status = 0;

	private boolean favorite = false;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantLogoUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantBrief;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantScore;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantPhone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String prompt;

	private long merchantUserScore = 0;

	private long merchantEventCount = 0;

	private long saleType = 0;

	private long bargainingStatus = 0;

	private long consultNum = 0L;

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEventTitle() {
		return eventTitle;
	}

	public void setEventTitle(String eventTitle) {
		this.eventTitle = eventTitle;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(String originalPrice) {
		this.originalPrice = originalPrice;
	}

	public String getEventTime() {
		return eventTime;
	}

	public void setEventTime(String eventTime) {
		this.eventTime = eventTime;
	}

	public long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(long commentNum) {
		this.commentNum = commentNum;
	}

	public long getSubscribeNum() {
		return subscribeNum;
	}

	public void setSubscribeNum(long subscribeNum) {
		this.subscribeNum = subscribeNum;
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

	public String[] getImageUrls() {
		return imageUrls;
	}

	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}

	public String getStatusString() {
		return statusString;
	}

	public void setStatusString(String statusString) {
		this.statusString = statusString;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getMerchantLogoUrl() {
		return merchantLogoUrl;
	}

	public void setMerchantLogoUrl(String merchantLogoUrl) {
		this.merchantLogoUrl = merchantLogoUrl;
	}

	public String getMerchantBrief() {
		return merchantBrief;
	}

	public void setMerchantBrief(String merchantBrief) {
		this.merchantBrief = merchantBrief;
	}

	public String getGpsLng() {
		return gpsLng;
	}

	public void setGpsLng(String gpsLng) {
		this.gpsLng = gpsLng;
	}

	public String getGpsLat() {
		return gpsLat;
	}

	public void setGpsLat(String gpsLat) {
		this.gpsLat = gpsLat;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public String getDetailHtmlUrl() {
		return detailHtmlUrl;
	}

	public void setDetailHtmlUrl(String detailHtmlUrl) {
		this.detailHtmlUrl = detailHtmlUrl;
	}

	public String getMerchantScore() {
		return merchantScore;
	}

	public void setMerchantScore(String merchantScore) {
		this.merchantScore = merchantScore;
	}

	public String getEventSubTitle() {
		return eventSubTitle;
	}

	public void setEventSubTitle(String eventSubTitle) {
		this.eventSubTitle = eventSubTitle;
	}

	public String getEventSubTitleImg() {
		return eventSubTitleImg;
	}

	public void setEventSubTitleImg(String eventSubTitleImg) {
		this.eventSubTitleImg = eventSubTitleImg;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getRecommend() {
		return recommend;
	}

	public void setRecommend(String recommend) {
		this.recommend = recommend;
	}

	public String getSiteAndDuration() {
		return siteAndDuration;
	}

	public void setSiteAndDuration(String siteAndDuration) {
		this.siteAndDuration = siteAndDuration;
	}

	public long getMerchantUserScore() {
		return merchantUserScore;
	}

	public void setMerchantUserScore(long merchantUserScore) {
		this.merchantUserScore = merchantUserScore;
	}

	public long getMerchantEventCount() {
		return merchantEventCount;
	}

	public void setMerchantEventCount(long merchantEventCount) {
		this.merchantEventCount = merchantEventCount;
	}

	public String getMerchantPhone() {
		return merchantPhone;
	}

	public void setMerchantPhone(String merchantPhone) {
		this.merchantPhone = merchantPhone;
	}

	public String getEventFocus() {
		return eventFocus;
	}

	public void setEventFocus(String eventFocus) {
		this.eventFocus = eventFocus;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public long getSaleType() {
		return saleType;
	}

	public void setSaleType(long saleType) {
		this.saleType = saleType;
	}

	public long getBargainingStatus() {
		return bargainingStatus;
	}

	public void setBargainingStatus(long bargainingStatus) {
		this.bargainingStatus = bargainingStatus;
	}

	public long getConsultNum() {
		return consultNum;
	}

	public void setConsultNum(long consultNum) {
		this.consultNum = consultNum;
	}

	public String getPrompt() {
		return prompt;
	}

	public void setPrompt(String prompt) {
		this.prompt = prompt;
	}

}