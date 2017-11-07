package com.innee.czyhInterface.dto;

import java.io.Serializable;

public class CustomerDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	// 提示消息
	private String msg;

	// 用户是否可用
	private boolean enable = false;

	// 状态码
	private int statusCode = 0;

	private String customerId;

	private String name;

	private String wxId;

	private String wxUnionId;

	private String wxName;

	private String photo;

	private String phone;

	private String baby;

	public String getBaby() {
		return baby;
	}

	public void setBaby(String baby) {
		this.baby = baby;
	}

	private int sex = 0;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWxId() {
		return wxId;
	}

	public void setWxId(String wxId) {
		this.wxId = wxId;
	}

	public String getWxUnionId() {
		return wxUnionId;
	}

	public void setWxUnionId(String wxUnionId) {
		this.wxUnionId = wxUnionId;
	}

	public String getWxName() {
		return wxName;
	}

	public void setWxName(String wxName) {
		this.wxName = wxName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

}