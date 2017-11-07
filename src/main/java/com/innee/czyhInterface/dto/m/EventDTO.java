package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class EventDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String type;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String tag;

	private int tagColor;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventTitle;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventSubTitle;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gps;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gpsLng;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gpsLat;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String originalPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String presentPrice;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String eventTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String distance;
	
	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String channelId;

	private BigDecimal score = BigDecimal.ZERO;

	private long recommend = 0L;

	private long commentNum = 0L;

	private long subscribeNum = 0L;

	private int stockFlag = 0;

	private int status = 0;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String statusString;

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

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public int getStockFlag() {
		return stockFlag;
	}

	public void setStockFlag(int stockFlag) {
		this.stockFlag = stockFlag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getTagColor() {
		return tagColor;
	}

	public void setTagColor(int tagColor) {
		this.tagColor = tagColor;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public long getRecommend() {
		return recommend;
	}

	public void setRecommend(long recommend) {
		this.recommend = recommend;
	}

	public String getEventSubTitle() {
		return eventSubTitle;
	}

	public void setEventSubTitle(String eventSubTitle) {
		this.eventSubTitle = eventSubTitle;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	

}