package com.innee.czyhInterface.dto.m.push;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.innee.czyhInterface.util.NullToEmptySerializer;

public class PushTokenDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String pushId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String pushCustomerId;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String token;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private String device;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Date createTime;

	@JsonSerialize(nullsUsing = NullToEmptySerializer.class)
	private Date updateTime;

	public String getPushId() {
		return pushId;
	}

	public void setPushId(String pushId) {
		this.pushId = pushId;
	}

	public String getPushCustomerId() {
		return pushCustomerId;
	}

	public void setPushCustomerId(String pushCustomerId) {
		this.pushCustomerId = pushCustomerId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}


}