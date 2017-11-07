package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_sms")
public class TSms extends IdEntity {

	private static final long serialVersionUID = 1L;

	// Fields
	private Date sendTime;
	private String sendPhone;
	private String smsContent;
	private Integer smsType;
	private Integer sendSuccess;
	private String sendResponse;

	// Constructors

	/** default constructor */
	public TSms() {
	}

	public TSms(Long id) {
		this.id = id;
	}

	// Property accessors
	@Column(name = "sendTime", length = 19)
	public Date getSendTime() {
		return this.sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "sendPhone", length = 2048)
	public String getSendPhone() {
		return this.sendPhone;
	}

	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}

	@Column(name = "smsContent")
	public String getSmsContent() {
		return this.smsContent;
	}

	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}

	@Column(name = "smsType")
	public Integer getSmsType() {
		return this.smsType;
	}

	public void setSmsType(Integer smsType) {
		this.smsType = smsType;
	}

	@Column(name = "sendSuccess")
	public Integer getSendSuccess() {
		return this.sendSuccess;
	}

	public void setSendSuccess(Integer sendSuccess) {
		this.sendSuccess = sendSuccess;
	}

	@Column(name = "sendResponse", length = 2048)
	public String getSendResponse() {
		return this.sendResponse;
	}

	public void setSendResponse(String sendResponse) {
		this.sendResponse = sendResponse;
	}

}