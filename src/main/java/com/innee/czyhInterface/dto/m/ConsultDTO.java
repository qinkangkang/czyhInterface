package com.innee.czyhInterface.dto.m;

import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class ConsultDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String consultId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String objectId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String customerName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String customerLogoUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String consultTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String content;

	private boolean replied = false;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String userName;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String userLogoUrl;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String replyTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String reply;

	public String getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}

	public boolean isReplied() {
		return replied;
	}

	public void setReplied(boolean replied) {
		this.replied = replied;
	}

	public String getConsultId() {
		return consultId;
	}

	public void setConsultId(String consultId) {
		this.consultId = consultId;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getConsultTime() {
		return consultTime;
	}

	public void setConsultTime(String consultTime) {
		this.consultTime = consultTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public String getCustomerLogoUrl() {
		return customerLogoUrl;
	}

	public void setCustomerLogoUrl(String customerLogoUrl) {
		this.customerLogoUrl = customerLogoUrl;
	}

	public String getUserLogoUrl() {
		return userLogoUrl;
	}

	public void setUserLogoUrl(String userLogoUrl) {
		this.userLogoUrl = userLogoUrl;
	}

}