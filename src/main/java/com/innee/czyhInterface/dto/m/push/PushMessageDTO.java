package com.innee.czyhInterface.dto.m.push;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class PushMessageDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String pushId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String customerId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String title;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String content;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String imageUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String targetType;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String targetObject;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String pushTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String pageTitle;

	private Integer validTimeStatus;

	private boolean unread = false;

	// private boolean hasImage = false;

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getPushTime() {
		return pushTime;
	}

	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}

	public boolean isUnread() {
		return unread;
	}

	public void setUnread(boolean unread) {
		this.unread = unread;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Integer getValidTimeStatus() {
		return validTimeStatus;
	}

	public void setValidTimeStatus(Integer validTimeStatus) {
		this.validTimeStatus = validTimeStatus;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public String getPageTitle() {
		return pageTitle;
	}

	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}

}