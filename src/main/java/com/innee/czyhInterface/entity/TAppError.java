package com.innee.czyhInterface.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "t_app_error")
public class TAppError extends UuidEntity {

	private static final long serialVersionUID = 1L;

	private Date freportTime;
	private Integer fclientType;
	private String fclientInfo;
	private String ferrorMessage;
	private String ferrorText;
	private String fsystem;
	private String fuser;
	private String fview;
	private String fdata;

	// Constructors

	/** default constructor */
	public TAppError() {
	}

	/** minimal constructor */
	public TAppError(String id) {
		this.id = id;
	}

	@Column(name = "fReportTime", length = 19)
	public Date getFreportTime() {
		return this.freportTime;
	}

	public void setFreportTime(Date freportTime) {
		this.freportTime = freportTime;
	}

	@Column(name = "fClientType")
	public Integer getFclientType() {
		return this.fclientType;
	}

	public void setFclientType(Integer fclientType) {
		this.fclientType = fclientType;
	}

	@Column(name = "fClientInfo", length = 2048)
	public String getFclientInfo() {
		return this.fclientInfo;
	}

	public void setFclientInfo(String fclientInfo) {
		this.fclientInfo = fclientInfo;
	}

	@Column(name = "fErrorMessage", length = 2048)
	public String getFerrorMessage() {
		return this.ferrorMessage;
	}

	public void setFerrorMessage(String ferrorMessage) {
		this.ferrorMessage = ferrorMessage;
	}

	@Column(name = "fErrorText", length = 2048)
	public String getFerrorText() {
		return this.ferrorText;
	}

	public void setFerrorText(String ferrorText) {
		this.ferrorText = ferrorText;
	}

	@Column(name = "fSystem")
	public String getFsystem() {
		return this.fsystem;
	}

	public void setFsystem(String fsystem) {
		this.fsystem = fsystem;
	}

	@Column(name = "fUser")
	public String getFuser() {
		return this.fuser;
	}

	public void setFuser(String fuser) {
		this.fuser = fuser;
	}

	@Column(name = "fView", length = 2048)
	public String getFview() {
		return this.fview;
	}

	public void setFview(String fview) {
		this.fview = fview;
	}

	@Column(name = "fData", length = 2048)
	public String getFdata() {
		return this.fdata;
	}

	public void setFdata(String fdata) {
		this.fdata = fdata;
	}

}