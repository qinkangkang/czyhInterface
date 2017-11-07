package com.innee.czyhInterface.dto.m;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class MerchantDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantFullName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String merchantLogoUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String brief;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String detail;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String detailHtmlUrl;

	private BigDecimal score = BigDecimal.ZERO;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String phone;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String address;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String gps;

	private boolean favorite = false;

	private boolean hasEvent = false;

	private List<CommentDTO> commentList;

	private boolean hasComment = false;

	private List<EventSimpleDTO> eventList;

	private List<EventSimpleDTO> overdueEventList;

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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isHasComment() {
		return hasComment;
	}

	public void setHasComment(boolean hasComment) {
		this.hasComment = hasComment;
	}

	public List<EventSimpleDTO> getEventList() {
		return eventList;
	}

	public void setEventList(List<EventSimpleDTO> eventList) {
		this.eventList = eventList;
	}

	public String getGps() {
		return gps;
	}

	public void setGps(String gps) {
		this.gps = gps;
	}

	public String getBrief() {
		return brief;
	}

	public void setBrief(String brief) {
		this.brief = brief;
	}

	public String getMerchantFullName() {
		return merchantFullName;
	}

	public void setMerchantFullName(String merchantFullName) {
		this.merchantFullName = merchantFullName;
	}

	public List<CommentDTO> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<CommentDTO> commentList) {
		this.commentList = commentList;
	}

	public boolean isHasEvent() {
		return hasEvent;
	}

	public void setHasEvent(boolean hasEvent) {
		this.hasEvent = hasEvent;
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

	public List<EventSimpleDTO> getOverdueEventList() {
		return overdueEventList;
	}

	public void setOverdueEventList(List<EventSimpleDTO> overdueEventList) {
		this.overdueEventList = overdueEventList;
	}

}